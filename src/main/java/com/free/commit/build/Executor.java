package com.free.commit.build;

import com.free.commit.build.exception.BuildException;
import com.free.commit.build.exception.SpecFileNotFoundException;
import com.free.commit.build.exception.SpecFileNotReadableException;
import com.free.commit.build.parser.SpecFile;
import com.free.commit.build.parser.Step;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.repository.BuildRepository;
import com.free.commit.repository.SecretRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Scope( "prototype" )
public class Executor {

    private         Build            build;
    private         Project          project;
    private         boolean          active = true;
    protected final BuildRepository  buildRepository;
    protected final SecretRepository secretRepository;


    public Executor(
            BuildRepository buildRepository,
            SecretRepository secretRepository ) {
        this.buildRepository  = buildRepository;
        this.secretRepository = secretRepository;
    }


    @Transactional
    public void execute( Project project, Build build ) {
        this.project = project;
        this.build   = build;

        initRepository();

        SpecFile specFile;

        try {
            specFile = getSpecFile();
        } catch ( BuildException e ) {
            build.setExitCode( e.getCode() )
                 .setExitMessage( ExitMessageMapper.MAPPER.get( e.getCode() ) );
            active = false;
            return;
        } catch ( Throwable e ) {
            build.addOutputLine( e.getMessage() )
                 .setExitCode( -1 )
                 .setExitMessage( ExitMessageMapper.MAPPER.get( -1 ) );
            active = false;
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
        active = false;

        buildRepository.persist( build );
    }


    public Build getBuild() {
        return build;
    }


    public Project getProject() {
        return project;
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
            Process process = Runtime.getRuntime()
                                     .exec( getLaunchContainerCommandLine( buildSpace, buildSpaceId ) );

            BufferedReader readerIn = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );

            BufferedReader readerErr = new BufferedReader(
                    new InputStreamReader( process.getErrorStream() ) );

            String lineIn;
            while ( (lineIn = readerIn.readLine()) != null ) {
                build.addOutputLine( lineIn );
            }

            String lineErr;
            while ( (lineErr = readerErr.readLine()) != null ) {
                build.addOutputLine( lineErr );
            }

            int exitCode = process.waitFor();

            build.setExitCode( exitCode )
                 .setExitMessage( ExitMessageMapper.MAPPER.get( exitCode ) );
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
                   .add( "./" + step.script.replaceFirst( "/", "" ) )
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

        stringJoiner.add( "cd /ci/build/" + buildSpaceId )
                    .add( "eval `ssh-agent`" )
                    .add( "echo \"" + project.getRepositoryCredential().getSshKey() + "\" | ssh-add -" )
                    .add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" )
                    .add( "git clone " + project.getRepository() + " app" )
                    .add( "cd app" )
                    .add( "git fetch --all" )
                    .add( "git checkout " + project.getBranch() )
                    .add( "git pull origin " + project.getBranch() )
                    .add( "cd .." )
                    .add( "docker build -t " + imageName + " ." );
        StringBuilder run = new StringBuilder( "docker run " + imageName + " " );

        for ( Secret secret : project.getSecrets() ) {
            run.append( " -e " + secret.getName() + "=" + secret.getValue() );
        }

        for ( Secret secret : secretRepository.findAllWithGlobalScope() ) {
            run.append( " -e " + secret.getName() + "=" + secret.getValue() );
        }

        run.append( " -v /var/run/docker.sock:/var/run/docker.sock" );

        stringJoiner.add( run.toString() );


        cmdline[ 2 ] = stringJoiner.toString();

        return cmdline;
    }


    protected void initRepository() {
        try {

            final String[] cmdline = getInitRepositoryCommandLines( project );

            Process process = Runtime.getRuntime()
                                     .exec( cmdline );

            BufferedReader readerIn = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );

            BufferedReader readerErr = new BufferedReader(
                    new InputStreamReader( process.getErrorStream() ) );

            String lineIn;
            while ( (lineIn = readerIn.readLine()) != null ) {
                build.addOutputLine( lineIn );
            }

            String lineErr;
            while ( (lineErr = readerErr.readLine()) != null ) {
                build.addOutputLine( lineErr );
            }

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

        stringJoiner.add( "eval `ssh-agent`" )
                    .add( "echo \"" + project.getRepositoryCredential().getSshKey() + "\" | ssh-add -" )
                    .add( "cd /ci/repository" )
                    .add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" );

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
}
