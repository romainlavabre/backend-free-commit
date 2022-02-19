package com.free.commit.module.developer;

import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.api.security.PasswordEncoder;
import com.free.commit.api.security.User;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createDeveloper" )
public class Create implements com.free.commit.api.crud.Create< Developer > {

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
        String         username       = ( String ) request.getParameter( DeveloperParameter.USERNAME );
        String         password       = ( String ) request.getParameter( DeveloperParameter.PASSWORD );
        List< Object > roles          = request.getParameters( DeveloperParameter.ROLES );
        String         email          = ( String ) request.getParameter( DeveloperParameter.EMAIL );
        String         githubUsername = ( String ) request.getParameter( DeveloperParameter.GITHUB_USERNAME );
        String         gitlabUsername = ( String ) request.getParameter( DeveloperParameter.GITLAB_USERNAME );

        User user = new User();
        user.setUsername( username );
        user.setPassword( passwordEncoder.encode( password ) );
        user.setEnabled( true );

        for ( Object role : roles ) {
            user.addRole( role.toString() );
        }

        developer
                .setGithubUsername( githubUsername )
                .setGitlabUsername( gitlabUsername )
                .setEmail( email )
                .setUser( user );

        developerRepository.persist( developer );

        historyHandler.create( developer );
    }
}
