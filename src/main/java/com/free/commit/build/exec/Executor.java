package com.free.commit.build.exec;

import com.free.commit.build.ExitMessageMapper;
import com.free.commit.build.Initiator;
import com.free.commit.build.exception.BuildException;
import com.free.commit.build.exception.SpecFileNotFoundException;
import com.free.commit.build.exception.SpecFileNotReadableException;
import com.free.commit.build.parser.SpecFile;
import com.free.commit.build.parser.Step;
import com.free.commit.configuration.environment.Variable;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Build;
import com.free.commit.entity.Log;
import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.repository.BuildRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.exception.HttpInternalServerErrorException;
import org.romainlavabre.exception.HttpNotFoundException;
import org.romainlavabre.mail.MailSender;
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

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "buildExecutor" )
@Scope( "prototype" )
public class Executor {

    private         Build             build;
    private         Project           project;
    private         Initiator         initiator;
    private         boolean           active = true;
    private         Process           currentProcess;
    private         String            imageId;
    private         String            requestBody;
    private         ZonedDateTime     launchedAt;
    protected final BuildRepository   buildRepository;
    protected final SecretRepository  secretRepository;
    protected final ProjectRepository projectRepository;
    protected final EntityManager     entityManager;
    protected final MailSender        mailSender;
    protected final Environment       environment;


    public Executor(
            BuildRepository buildRepository,
            SecretRepository secretRepository,
            ProjectRepository projectRepository,
            EntityManager entityManager,
            MailSender mailSender,
            Environment environment ) {
        this.buildRepository   = buildRepository;
        this.secretRepository  = secretRepository;
        this.projectRepository = projectRepository;
        this.entityManager     = entityManager;
        this.mailSender        = mailSender;
        this.environment       = environment;
    }


    @Transactional
    public void execute( Project project, Build build, Initiator initiator, String requestBody, List< String > ignoreSteps ) {
        launchedAt       = ZonedDateTime.now();
        this.project     = projectRepository.findOrFail( project.getId() );
        this.build       = build;
        this.initiator   = initiator;
        this.requestBody = requestBody;


        initRepository();

        SpecFile specFile;

        try {
            specFile = getSpecFile();
        } catch ( BuildException e ) {
            build
                    .setExitCode( e.getCode() )
                    .setExitMessage( ExitMessageMapper.MAPPER.get( e.getCode() ) )
                    .setProject( this.project );
            active = false;
            entityManager.persist( build );
            launchEmail( build );
            return;
        } catch ( Throwable e ) {
            build
                    .addOutputLine( e.getMessage() )
                    .setExitCode( -1 )
                    .setExitMessage( ExitMessageMapper.MAPPER.get( -1 ) )
                    .setProject( this.project );
            active = false;
            entityManager.persist( build );
            launchEmail( build );
            return;
        }

        String directoryId = UUID.randomUUID().toString();
        Path   buildSpace  = Path.of( "/ci/build/" + directoryId );
        Path   entrypoint  = Path.of( "/ci/build/" + directoryId + "/entrypoint.sh" );

        try {
            Files.createDirectory( buildSpace );
            Files.createFile( entrypoint );
            Files.write( entrypoint, getEntryPointContent( specFile, ignoreSteps ) );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        createDockerFile( specFile, buildSpace );

        launchContainer( directoryId );
        entityManager.persist( build );

        if ( project.getKeepNumberBuild() != null ) {
            List< Build > current = new ArrayList<>( this.project.getBuilds() );
            Collections.reverse( current );

            this.project.getBuilds().clear();

            for ( int i = 0; i < this.project.getKeepNumberBuild() && i < current.size(); i++ ) {
                this.project.addBuild( current.get( i ) );
            }
        }

        launchEmail( build );

        active = false;
    }


    public Build getBuild() {
        return build;
    }


    public Project getProject() {
        return project;
    }


    public void kill() {
        if ( currentProcess != null ) {
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


            build.addLog( new Log( "abort" ).addLine( "[WARNING] Container killed" ) ).addOutputLine( "[WARNING] Container killed" );
        }

        active = false;
    }


    public boolean isActive() {
        return active;
    }


    protected void createDockerFile( SpecFile specFile, Path buildSpace ) {
        Path dockerFile = Path.of( buildSpace.toString() + "/Dockerfile" );


        StringJoiner content = new StringJoiner( "\n" );
        content
                .add( "FROM " + specFile.from )
                .add( "COPY . ." )
                .add( "COPY app/ app/" )
                .add( "RUN chmod +x entrypoint.sh" )
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
            ProcessBuilder builder = new ProcessBuilder( getLaunchContainerCommandLine( buildSpaceId ) );
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
                .add( "cd /app" );

        project.getAvailableSteps().clear();

        for ( Step step : specFile.steps ) {
            project.addAvailableStep( step.name );

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


    protected String[] getLaunchContainerCommandLine( String buildSpaceId ) {
        String[] cmdline = new String[ 3 ];
        cmdline[ 0 ] = "sh";
        cmdline[ 1 ] = "-c";
        StringJoiner stringJoiner = new StringJoiner( " && " );

        String imageName = imageId = UUID.randomUUID().toString();

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
                .add( "docker build -t " + imageName + " ." );

        StringBuilder run = new StringBuilder( "docker run --user root" );

        for ( Secret secret : project.getSecrets() ) {
            run.append( " -e \"" + secret.getName() + "=" + escapeSecret( secret ) + "\"" );
        }

        for ( Secret secret : secretRepository.findAllWithGlobalScope() ) {
            run.append( " -e \"" + secret.getName() + "=" + escapeSecret( secret ) + "\"" );
        }

        run.append( " -e FREE_COMMIT_REQUEST_BODY='" + Base64.getEncoder().encodeToString( requestBody.getBytes() ) + "'" );

        run.append( " -v /var/run/docker.sock:/var/run/docker.sock " );

        run.append( imageName );

        stringJoiner.add( run.toString() );
        stringJoiner.add( "docker image rm " + imageName + " -f" );

        cmdline[ 2 ] = stringJoiner.toString();

        return cmdline;
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


    protected void launchEmail( Build build ) {
        if ( build.getExitCode() != 0 && initiator.getEmail() != null ) {


            mailSender.send(
                    environment.getEnv( Variable.MAIL_FROM ),
                    initiator.getEmail(),
                    "[" + project.getName().toUpperCase() + "] Build Failure (#" + build.getId() + ")",
                    "Build #" + build.getId() + " failure. (project " + project.getName().toUpperCase() + ")\r\r\r\r" + build.getOutput()
            );
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
