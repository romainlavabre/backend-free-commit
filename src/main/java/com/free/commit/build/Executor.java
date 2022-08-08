package com.free.commit.build;

import com.free.commit.api.mail.MailSender;
import com.free.commit.build.exception.BuildException;
import com.free.commit.build.exception.SpecFileNotFoundException;
import com.free.commit.build.exception.SpecFileNotReadableException;
import com.free.commit.build.parser.SpecFile;
import com.free.commit.build.parser.Step;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.repository.BuildRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Scope( "prototype" )
public class Executor {

    private         Build             build;
    private         Project           project;
    private         Initiator         initiator;
    private         boolean           active = true;
    private         Process           currentProcess;
    protected final BuildRepository   buildRepository;
    protected final SecretRepository  secretRepository;
    protected final ProjectRepository projectRepository;
    protected final EntityManager     entityManager;
    protected final MailSender        mailSender;


    public Executor(
            BuildRepository buildRepository,
            SecretRepository secretRepository,
            ProjectRepository projectRepository,
            EntityManager entityManager,
            MailSender mailSender ) {
        this.buildRepository   = buildRepository;
        this.secretRepository  = secretRepository;
        this.projectRepository = projectRepository;
        this.entityManager     = entityManager;
        this.mailSender        = mailSender;
    }


    @Transactional
    public void execute( Project project, Build build, Initiator initiator ) {
        this.project   = projectRepository.findOrFail( project.getId() );
        this.build     = build;
        this.initiator = initiator;


        initRepository();

        SpecFile specFile;

        try {
            specFile = getSpecFile();
        } catch ( BuildException e ) {
            build.setExitCode( e.getCode() )
                 .setExitMessage( ExitMessageMapper.MAPPER.get( e.getCode() ) )
                 .setProject( this.project );
            active = false;
            entityManager.persist( build );
            launchEmail( build );
            return;
        } catch ( Throwable e ) {
            build.addOutputLine( e.getMessage() )
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
            Files.write( entrypoint, getEntryPointContent( specFile ) );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        createDockerFile( specFile, buildSpace );

        launchContainer( buildSpace, directoryId );
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
        }

        active = false;
    }


    public boolean isActive() {
        return active;
    }


    protected void createDockerFile( SpecFile specFile, Path buildSpace ) {
        Path dockerFile = Path.of( buildSpace.toString() + "/Dockerfile" );


        StringJoiner content = new StringJoiner( "\n" );
        content.add( "FROM " + specFile.from )
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


    protected void launchContainer( Path buildSpace, String buildSpaceId ) {
        try {
            ProcessBuilder builder = new ProcessBuilder( getLaunchContainerCommandLine( buildSpace, buildSpaceId ) );
            builder.redirectErrorStream( true );
            Process process = builder.start();

            currentProcess = process;

            BufferedReader readerIn = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );


            String lineIn;
            while ( (lineIn = readerIn.readLine()) != null ) {
                if ( lineIn.equals( "null" ) ) {
                    continue;
                }

                build.addOutputLine( lineIn );
            }

            int exitCode = process.waitFor();

            build.setExitCode( exitCode )
                 .setExitMessage( ExitMessageMapper.MAPPER.get( exitCode ) )
                 .setProject( this.project );
        } catch ( IOException | InterruptedException e ) {
            e.printStackTrace();
        }
    }


    protected byte[] getEntryPointContent( SpecFile specFile ) {
        StringJoiner content = new StringJoiner( "\n" );

        content.add( "#!/bin/sh" )
               .add( "assertLastCmdSuccess() {" )
               .add( "if [ \"$?\" != \"0\" ]; then" )
               .add( "echo \"$1\" && exit 2000" )
               .add( "fi" )
               .add( "}" )
               .add( "cd /app" );

        for ( Step step : specFile.steps ) {
            content.add( "" )
                   .add( "echo 'Step " + step.name + " ...'" )
                   .add( "chmod +x /app/" + step.script.replaceFirst( "/", "" ) )
                   .add( ". " + step.script.replaceFirst( "/", "" ) )
                   .add( "assertLastCmdSuccess 'Step " + step.name + " failed'" );
        }

        return content.toString().getBytes();
    }


    protected SpecFile getSpecFile()
            throws BuildException {
        Path path = Path.of( "/ci/repository/" + project.getName() + "/" + project.getSpecFilePath().replaceFirst( "/", "" ) );

        if ( Files.exists( path ) ) {
            Yaml yaml = new Yaml( new Constructor( SpecFile.class ) );
            try {
                return yaml.load( Files.readString( path ) );
            } catch ( IOException e ) {
                throw new SpecFileNotReadableException();
            }
        }

        throw new SpecFileNotFoundException();
    }


    protected String[] getLaunchContainerCommandLine( Path buildSpace, String buildSpaceId ) {
        String[] cmdline = new String[ 3 ];
        cmdline[ 0 ] = "sh";
        cmdline[ 1 ] = "-c";
        StringJoiner stringJoiner = new StringJoiner( " && " );

        String imageName = UUID.randomUUID().toString();

        stringJoiner.add( "cd /ci/build/" + buildSpaceId );

        if ( project.getRepositoryCredential() != null ) {
            stringJoiner.add( "eval `ssh-agent`" )
                        .add( "echo \"" + project.getRepositoryCredential().getSshKey() + "\" | ssh-add -" )
                        .add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" );
        }

        stringJoiner.add( "git clone --quiet " + project.getRepository() + " app > /dev/null" )
                    .add( "cd app" )
                    .add( "git fetch --all -q > /dev/null 2>&1" )
                    .add( "git checkout -q " + project.getBranch() + " > /dev/null" )
                    .add( "git pull --ff-only origin " + project.getBranch() )
                    .add( "cd .." )
                    .add( "docker build -t " + imageName + " ." );

        StringBuilder run = new StringBuilder( "docker run " );

        for ( Secret secret : project.getSecrets() ) {
            run.append( " -e \"" + secret.getName() + "=" + escapeSecret( secret ) + "\"" );
        }

        for ( Secret secret : secretRepository.findAllWithGlobalScope() ) {
            run.append( " -e \"" + secret.getName() + "=" + escapeSecret( secret ) + "\"" );
        }

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
                        .add( "cd /ci/repository" )
                        .add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" );
        }

        if ( !Files.exists( Path.of( "/ci/repository/" + project.getName() ) ) ) {
            stringJoiner.add( "git clone " + project.getRepository() + " " + project.getName() );
        }

        stringJoiner.add( "cd /ci/repository/" + project.getName() )
                    .add( "git fetch --all" )
                    .add( "git checkout " + project.getBranch() )
                    .add( "git pull origin " + project.getBranch() );

        cmdline[ 2 ] = stringJoiner.toString();

        return cmdline;
    }


    protected void launchEmail( Build build ) {
        if ( build.getExitCode() != 0 && initiator.getEmail() != null ) {
            mailSender.send(
                    initiator.getEmail(),
                    "[" + project.getName().toUpperCase() + "] Build Failure (#" + build.getId() + ")",
                    "Build #" + build.getId() + " failure. (project " + project.getName().toUpperCase() + ")"
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
