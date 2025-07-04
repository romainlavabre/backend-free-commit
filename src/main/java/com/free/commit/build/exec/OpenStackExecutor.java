package com.free.commit.build.exec;

import com.free.commit.build.BuildManager;
import com.free.commit.build.ExitMessageMapper;
import com.free.commit.build.Initiator;
import com.free.commit.build.exception.BuildException;
import com.free.commit.build.exception.SpecFileNotFoundException;
import com.free.commit.build.exception.SpecFileNotReadableException;
import com.free.commit.build.parser.SpecFile;
import com.free.commit.build.parser.Step;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Build;
import com.free.commit.entity.Log;
import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.module.buildhistory.BuildHistoryBuilder;
import com.free.commit.repository.BuildRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.exception.HttpInternalServerErrorException;
import org.romainlavabre.exception.HttpNotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service( "OpenStackExecutor" )
@Scope( "prototype" )
public class OpenStackExecutor implements Executor {
    private         Build             build;
    private         Project           project;
    private         Initiator         initiator;
    private         boolean           active = true;
    private         Process           currentProcess;
    private         String            imageId;
    private         String            requestBody;
    private         ZonedDateTime     launchedAt;
    private         SpecFile          specFile;
    private         String            openVpnClient;
    protected final BuildRepository   buildRepository;
    protected final SecretRepository  secretRepository;
    protected final ProjectRepository projectRepository;
    protected final EntityManager     entityManager;
    protected final Environment       environment;
    protected final BuildManager      buildManager;


    public OpenStackExecutor(
            BuildRepository buildRepository,
            SecretRepository secretRepository,
            ProjectRepository projectRepository,
            EntityManager entityManager,
            Environment environment,
            BuildManager buildManager ) {
        this.buildRepository   = buildRepository;
        this.secretRepository  = secretRepository;
        this.projectRepository = projectRepository;
        this.entityManager     = entityManager;
        this.environment       = environment;
        this.buildManager      = buildManager;
    }


    @Transactional
    public void execute( Project project, Build build, Initiator initiator, String requestBody, List< String > ignoreSteps ) {
        launchedAt       = ZonedDateTime.now();
        this.project     = projectRepository.findOrFail( project.getId() );
        this.build       = build;
        this.initiator   = initiator;
        this.requestBody = requestBody;

        try {
            lockOpenVpnClient();
        } catch ( IOException ignored ) {
        }


        initRepository();


        try {
            specFile = getSpecFile();
        } catch ( BuildException e ) {
            build
                    .setExitCode( e.getCode() )
                    .setExitMessage( ExitMessageMapper.MAPPER.get( e.getCode() ) )
                    .setProject( this.project );
            active = false;
            entityManager.persist( build );
            BuildHistoryBuilder.build( build, entityManager );
            return;
        } catch ( Throwable e ) {
            build
                    .addOutputLine( e.getMessage() )
                    .setExitCode( -1 )
                    .setExitMessage( ExitMessageMapper.MAPPER.get( -1 ) )
                    .setProject( this.project );
            active = false;
            entityManager.persist( build );
            BuildHistoryBuilder.build( build, entityManager );
            return;
        }

        String directoryId = UUID.randomUUID().toString();
        Path   buildSpace  = Path.of( "/ci/build/" + directoryId );
        Path   entrypoint  = Path.of( "/ci/build/" + directoryId + "/main-entrypoint.sh" );

        try {
            Files.createDirectory( buildSpace );
            Files.createFile( entrypoint );
            copyOpenVpnClient( directoryId );
            Files.write( entrypoint, getEntryPointContent( specFile, ignoreSteps ) );
            writeLaunchContainerCommandLine( specFile, buildSpace );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        createDockerFile( buildSpace );

        launchContainer( directoryId );
        entityManager.persist( build );
        BuildHistoryBuilder.build( build, entityManager );

        if ( project.getKeepNumberBuild() != null ) {
            List< Build > current = new ArrayList<>( this.project.getBuilds() );
            Collections.reverse( current );

            this.project.getBuilds().clear();

            for ( int i = 0; i < this.project.getKeepNumberBuild() && i < current.size(); i++ ) {
                this.project.addBuild( current.get( i ) );
            }
        }

        active = false;
    }


    public Build getBuild() {
        return build;
    }


    public Project getProject() {
        return project;
    }


    @Override
    public void kill() {
        if ( currentProcess != null ) {
            try {
                ProcessBuilder builder = new ProcessBuilder( "sh", "-c", "docker exec " + imageId + " bash -c \". cleanup.sh\" && docker container stop " + imageId + " -s SIGKILL" );
                builder.redirectErrorStream( true );
                Process        process  = builder.start();
                BufferedReader readerIn = new BufferedReader( new InputStreamReader( process.getInputStream() ) );


                String lineIn;
                while ( ( lineIn = readerIn.readLine() ) != null ) {
                    if ( lineIn.equals( "null" ) ) {
                        continue;
                    }

                    System.out.println( "Kill result : " + lineIn );
                }

                process.waitFor();
            } catch ( IOException | InterruptedException ignored ) {
                ignored.printStackTrace();
            }

            currentProcess.destroy();

            List< String > lines = new ArrayList<>();

            try {
                ProcessBuilder builder = new ProcessBuilder( "sh", "-c", "docker container ls" );
                builder.redirectErrorStream( true );
                Process process = builder.start();

                BufferedReader readerIn = new BufferedReader(
                        new InputStreamReader( process.getInputStream() ) );


                String lineIn;
                while ( ( lineIn = readerIn.readLine() ) != null ) {
                    if ( lineIn.equals( "null" ) ) {
                        continue;
                    }

                    lines.add( lineIn );
                }

                process.waitFor();
            } catch ( IOException | InterruptedException e ) {
                e.printStackTrace();
                throw new HttpInternalServerErrorException( "INTERNAL_SERVER_ERROR" );
            }

            String containerId = null;

            for ( String line : lines ) {
                if ( line.contains( imageId ) ) {
                    containerId = line.split( " " )[ 0 ].trim();
                }
            }

            if ( containerId == null ) {
                if ( launchedAt.toEpochSecond() + 20 >= ZonedDateTime.now().toEpochSecond() ) {
                    throw new HttpNotFoundException( Message.CONTAINER_NOT_FOUND );
                }
            } else {
                try {
                    ProcessBuilder builder = new ProcessBuilder( "sh", "-c", "docker container rm " + containerId + " -f" );
                    builder.redirectErrorStream( true );
                    Process process = builder.start();
                    process.waitFor();
                } catch ( IOException | InterruptedException e ) {
                    e.printStackTrace();
                    throw new HttpInternalServerErrorException( "INTERNAL_SERVER_ERROR" );
                }
            }


            Log log = new Log( "abort" ).addLine( "[WARNING] Container killed" ).setSuccess( false );
            log.close();

            build.addLog( log ).addOutputLine( "[WARNING] Container killed" );
        }

        active = false;
    }


    @Override
    public String getOpenVpnClient() {
        return openVpnClient;
    }


    public boolean isActive() {
        return active;
    }


    protected void createDockerFile( Path buildSpace ) {
        Path dockerFile = Path.of( buildSpace.toString() + "/Dockerfile" );


        StringJoiner content = new StringJoiner( "\n" );
        content
                .add( "FROM romainlavabre/free-commit-open-stack-driver" )
                .add( "COPY . ." )
                .add( "COPY app/ app/" )
                .add( "ENTRYPOINT [\"./entrypoint.sh\"]" );

        try {
            Files.createFile( dockerFile );
            Files.write( dockerFile, content.toString().getBytes() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }


    protected void launchContainer( String buildSpaceId ) {
        try {
            ProcessBuilder builder = new ProcessBuilder( getLaunchDriverContainerCommandLine( buildSpaceId ) );
            builder.redirectErrorStream( true );
            Process process = builder.start();

            currentProcess = process;

            int exitCode = LogCompiler.compile( process, build );

            build
                    .setExitCode( exitCode )
                    .setExitMessage( ExitMessageMapper.MAPPER.get( exitCode ) )
                    .setProject( this.project );
        } catch ( IOException | InterruptedException e ) {
            e.printStackTrace();
        }
    }


    protected byte[] getEntryPointContent( SpecFile specFile, List< String > ignoreSteps ) {
        StringJoiner content = new StringJoiner( "\n" );

        content
                .add( "#!/bin/bash" )
                .add( "assertLastCmdSuccess() {" )
                .add( "if [ \"$?\" != \"0\" ]; then" )
                .add( "echo \"$1\" && exit 2000" )
                .add( "fi" )
                .add( "}" )
                .add( "cd /app" )
                .add( "git config --global --add safe.directory /app" );

        project.getAvailableSteps().clear();

        for ( Step step : specFile.steps ) {
            project.addAvailableStep( step.name );

            if ( Objects.equals( step.name, "@cleanup" ) ) {
                continue;
            }


            if ( ignoreSteps.contains( step.name ) ) {
                content
                        .add( "" )
                        .add( "echo 'Step " + step.name + " ...'" )
                        .add( "echo 'Step " + step.name + " skipped'" );
            } else {
                content
                        .add( "" )
                        .add( "echo 'Step " + step.name + " ...'" )
                        .add( "chmod +x /app/" + step.script.replaceFirst( "/", "" ).split( " " )[ 0 ] )
                        .add( ". " + step.script.replaceFirst( "/", "" ) )
                        .add( "assertLastCmdSuccess 'Step " + step.name + " failed'" );
            }

        }

        return content.toString().getBytes();
    }


    protected SpecFile getSpecFile()
            throws BuildException {
        Path path = Path.of( "/ci/repository/" + project.getName() + "/" + project.getSpecFilePath().replaceFirst( "/", "" ) );

        if ( Files.exists( path ) ) {
            Yaml yaml = new Yaml( new Constructor( new LoaderOptions() ) );

            try {
                return yaml.loadAs( Files.readString( path ), SpecFile.class );
            } catch ( IOException e ) {
                throw new SpecFileNotReadableException();
            }
        }

        throw new SpecFileNotFoundException();
    }


    protected String[] getLaunchDriverContainerCommandLine( String buildSpaceId ) {
        String[] cmdline = new String[ 3 ];
        cmdline[ 0 ] = "sh";
        cmdline[ 1 ] = "-c";
        StringJoiner stringJoiner = new StringJoiner( " && " );

        imageId = UUID.randomUUID().toString();

        stringJoiner.add( "cd /ci/build/" + buildSpaceId );

        if ( project.getRepositoryCredential() != null ) {
            stringJoiner.add( "eval `ssh-agent`" )
                    .add( "echo \"" + project.getRepositoryCredential().getSshKey() + "\" | ssh-add -" )
                    .add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" );
        }

        stringJoiner.add( "git clone --quiet " + project.getRepository() + " app > /dev/null" )
                .add( "cd app" )
                .add( "git fetch --all -q > /dev/null 2>&1" )
                .add( "git checkout -q " + ( project.getBranch().equals( "*" ) ? "master" : project.getBranch() ) + " > /dev/null" )
                .add( "git pull --ff-only origin " + ( project.getBranch().equals( "*" ) ? "master" : project.getBranch() ) )
                .add( "cd .." )
                .add( "docker login --username=\"" + project.getExecutor().getVariables().get( "DOCKER_HUB_USERNAME" ) + "\" --password=\"" + project.getExecutor().getVariables().get( "DOCKER_HUB_PASSWORD" ) + "\" > /dev/null" )
                .add( "docker build -t " + imageId + " ." );

        StringBuilder run = new StringBuilder( "docker run --name " + imageId + " --user root" );

        for ( Object key : project.getExecutor().getVariables().keySet() ) {
            run.append( " -e \"" + key.toString() + "=" + project.getExecutor().getVariables().get( key.toString() ) + "\"" );
        }

        run.append( " -e FREE_COMMIT_EXECUTOR_ID=FC-node-" + imageId );

        run.append( " -v /var/run/docker.sock:/var/run/docker.sock " );

        run.append( imageId );

        stringJoiner.add( run.toString() );
        stringJoiner.add( "docker container rm " + imageId );
        stringJoiner.add( "docker image rm " + imageId );

        cmdline[ 2 ] = stringJoiner.toString();

        return cmdline;
    }


    protected void writeLaunchContainerCommandLine( SpecFile specFile, Path buildSpace ) throws IOException {
        StringJoiner stringJoiner = new StringJoiner( " && " );

        imageId = UUID.randomUUID().toString();


        if ( project.getRepositoryCredential() != null ) {
            stringJoiner.add( "eval `ssh-agent`" )
                    .add( "echo \"" + project.getRepositoryCredential().getSshKey() + "\" | ssh-add -" )
                    .add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" );
        }

        stringJoiner.add( "git clone --quiet " + project.getRepository() + " app > /dev/null" )
                .add( "cd app" )
                .add( "git fetch --all -q > /dev/null 2>&1" )
                .add( "git checkout -q " + ( project.getBranch().equals( "*" ) ? "master" : project.getBranch() ) + " > /dev/null" )
                .add( "git pull --ff-only origin " + ( project.getBranch().equals( "*" ) ? "master" : project.getBranch() ) )
                .add( "cd .." )
                .add( "chmod +x ./entrypoint.sh" )
                .add( "docker login --username=\"" + project.getExecutor().getVariables().get( "DOCKER_HUB_USERNAME" ) + "\" --password=\"" + project.getExecutor().getVariables().get( "DOCKER_HUB_PASSWORD" ) + "\" > /dev/null" )
                .add( "docker build -t main ." );

        StringBuilder run = new StringBuilder( "docker run --name main --user root" );

        for ( Secret secret : project.getSecrets() ) {
            run.append( " -e \"" + secret.getName() + "=" + escapeSecret( secret ) + "\"" );
        }

        for ( Secret secret : secretRepository.findAllWithGlobalScope() ) {
            run.append( " -e \"" + secret.getName() + "=" + escapeSecret( secret ) + "\"" );
        }

        run.append( " -e FREE_COMMIT_REQUEST_BODY='" + Base64.getEncoder().encodeToString( requestBody.getBytes() ) + "'" );

        run.append( " -v /var/run/docker.sock:/var/run/docker.sock " );
        run.append( "main" );

        stringJoiner.add( run.toString() );

        Files.write( Path.of( buildSpace.toString() + "/launch.sh" ), stringJoiner.toString().getBytes() );

        String specFilePath = null;

        for ( Step step : specFile.steps ) {
            if ( Objects.equals( step.name, "@cleanup" ) ) {
                specFilePath = step.script.replaceFirst( "/", "" );
            }
        }

        if ( specFilePath != null ) {
            String fileContent = "#!/bin/bash\nssh -o ConnectTimeout=10 ubuntu@\"$1\" 'docker exec main bash -c \"cd /app && . " + specFilePath + "\"'";

            Files.write( Path.of( buildSpace.toString() + "/remote-cleanup.sh" ), fileContent.getBytes() );
        }

        StringJoiner content = new StringJoiner( "\n" );
        content
                .add( "FROM " + specFile.from )
                .add( "ADD app/ app/" )
                .add( "ADD entrypoint.sh entrypoint.sh" )
                .add( "RUN chmod +x entrypoint.sh" )
                .add( "ENTRYPOINT [\"./entrypoint.sh\"]" );

        try {
            Files.write( Path.of( buildSpace.toString() + "/main-Dockerfile" ), content.toString().getBytes() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }


    protected void initRepository() {
        try {
            final String[] cmdline = getInitRepositoryCommandLines( project );

            ProcessBuilder builder = new ProcessBuilder( cmdline );
            builder.redirectErrorStream( true );
            Process process = builder.start();

            currentProcess = process;

            process.waitFor();
        } catch ( IOException | InterruptedException e ) {
            e.printStackTrace();
        }
    }


    protected String[] getInitRepositoryCommandLines( Project project ) {
        String[] cmdline = new String[ 3 ];
        cmdline[ 0 ] = "sh";
        cmdline[ 1 ] = "-c";
        StringJoiner stringJoiner = new StringJoiner( " && " );

        if ( project.getRepositoryCredential() != null ) {
            stringJoiner.add( "eval `ssh-agent`" )
                    .add( "echo \"" + project.getRepositoryCredential().getSshKey() + "\" | ssh-add -" )
                    .add( "cd /ci/repository" );
        }

        stringJoiner.add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" );

        if ( !Files.exists( Path.of( "/ci/repository/" + project.getName() ) ) ) {
            stringJoiner.add( "git clone " + project.getRepository() + " " + project.getName() );
        }

        stringJoiner.add( "cd /ci/repository/" + project.getName() )
                .add( "git fetch --all" )
                .add( "git checkout " + ( project.getBranch().equals( "*" ) ? "master" : project.getBranch() ) )
                .add( "git pull origin " + ( project.getBranch().equals( "*" ) ? "master" : project.getBranch() ) );

        cmdline[ 2 ] = stringJoiner.toString();

        return cmdline;
    }


    protected void copyOpenVpnClient( String directoryId ) throws IOException {
        if ( openVpnClient == null ) {
            return;
        }

        Files.copy( Path.of( openVpnClient ), Path.of( "/ci/build/" + directoryId + "/client.ovpn" ) );
        Files.writeString( Path.of( "/ci/build/" + directoryId + "/client-used" ), openVpnClient );
    }


    protected void lockOpenVpnClient() throws IOException {
        Path ovpnDir = Path.of( "/ovpn" );

        if ( !Files.isDirectory( ovpnDir ) ) {
            return;
        }

        Stream< Path > files = null;

        try {
            files = Files.list( ovpnDir );

            boolean        containOvpnClient = false;
            List< String > available         = new ArrayList<>();

            for ( Path file : files.toList() ) {
                if ( file.toString().contains( ".ovpn" ) ) {
                    containOvpnClient = true;
                    available.add( file.toString() );
                }
            }

            if ( !containOvpnClient ) {
                return;
            }

            List< String > openVpnClientUsed = buildManager.getOpenVpnClientUsed();

            for ( String client : available ) {
                if ( !openVpnClientUsed.contains( client ) ) {
                    openVpnClient = client;
                    return;
                }
            }
        } finally {
            if ( files != null ) {
                files.close();
            }
        }
    }


    protected String escapeSecret( Secret secret ) {
        if ( secret.getEscapeChar() == null || secret.getEscapeChar().isBlank() ) {
            return secret.getValue();
        }

        String initial = secret.getValue();

        for ( String toEscape : secret.getEscapeChar().split( "," ) ) {
            initial = initial.replace( toEscape, "\\" + toEscape );
        }

        return initial;
    }
}
