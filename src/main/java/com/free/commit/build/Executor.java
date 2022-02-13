package com.free.commit.build;

import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.repository.BuildRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Scope( "prototype" )
public class Executor {

    private         Build           build;
    private         Project         project;
    protected final BuildRepository buildRepository;


    public Executor( BuildRepository buildRepository ) {
        this.buildRepository = buildRepository;
    }


    public void execute( Project project, Build build ) {
        this.project = project;
        this.build   = build;

        initRepository();
    }


    public Build getBuild() {
        return build;
    }


    public Project getProject() {
        return project;
    }


    protected void initRepository() {
        try {

            final String[] cmdline = getInitRepositoryCommandLines( project );

            Process process = Runtime.getRuntime()
                                     .exec( cmdline );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader( process.getInputStream() ) );

            String line;
            while ( (line = reader.readLine()) != null ) {
                build.addOutputLine( line );
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
                    .add( "cd /ci" );

        if ( !Files.exists( Path.of( "/ci/" + project.getName() ) ) ) {
            stringJoiner.add( "export GIT_SSH_COMMAND=\"ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no\"" )
                        .add( "git clone " + project.getRepository() + " " + project.getName() );
        }

        stringJoiner.add( "cd " + project.getName() )
                    .add( "git checkout " + project.getBranch() )
                    .add( "git pull origin " + project.getBranch() );

        cmdline[ 2 ] = stringJoiner.toString();

        return cmdline;
    }
}
