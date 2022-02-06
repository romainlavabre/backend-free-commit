package com.free.commit.module.developer;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperGithubUsername" )
public class UpdateGithubUsername implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;


    public UpdateGithubUsername(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
    }


    @Override
    public void update( Request request, Developer developer ) {
        String githubUsername = ( String ) request.getParameter( DeveloperParameter.GITHUB_USERNAME );

        developer.setGithubUsername( githubUsername );

        historyHandler.update( developer, DeveloperProperty.GITHUB_USERNAME );
        
        developerRepository.persist( developer );
    }
}
