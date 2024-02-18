package com.free.commit.module.developer;

import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperGitlabUsername" )
public class UpdateGitlabUsername implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;


    public UpdateGitlabUsername(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
    }


    @Override
    public void update( Request request, Developer developer ) {
        String gitlabUsername = ( String ) request.getParameter( DeveloperParameter.GITLAB_USERNAME );

        developer.setGitlabUsername( gitlabUsername );

        historyHandler.update( developer, DeveloperProperty.GITLAB_USERNAME );

        developerRepository.persist( developer );
    }
}
