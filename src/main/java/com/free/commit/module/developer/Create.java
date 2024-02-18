package com.free.commit.module.developer;

import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.repository.DeveloperRepository;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.romainlavabre.security.PasswordEncoder;
import org.romainlavabre.security.User;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createDeveloper" )
public class Create implements org.romainlavabre.crud.Create< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;
    protected final PasswordEncoder     passwordEncoder;


    public Create(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler,
            PasswordEncoder passwordEncoder ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
        this.passwordEncoder     = passwordEncoder;
    }


    @Override
    public void create( Request request, Developer developer ) {
        String username       = ( String ) request.getParameter( DeveloperParameter.USERNAME );
        String password       = ( String ) request.getParameter( DeveloperParameter.PASSWORD );
        String role           = request.getParameter( DeveloperParameter.ROLE, String.class );
        String email          = ( String ) request.getParameter( DeveloperParameter.EMAIL );
        String githubUsername = ( String ) request.getParameter( DeveloperParameter.GITHUB_USERNAME );
        String gitlabUsername = ( String ) request.getParameter( DeveloperParameter.GITLAB_USERNAME );

        User user = new User();
        user.setUsername( username );
        user.setPassword( passwordEncoder.encode( password ) );
        user.setEnabled( true );
        user.addRole( role );

        developer
                .setGithubUsername( githubUsername )
                .setGitlabUsername( gitlabUsername )
                .setEmail( email )
                .setUser( user );

        developerRepository.persist( developer );

        historyHandler.create( developer );
    }
}
