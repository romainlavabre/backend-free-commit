package com.free.commit.module.developer;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperRoles" )
public class UpdateRoles implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;
    protected final ProjectRepository   projectRepository;


    public UpdateRoles(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
        this.projectRepository   = projectRepository;
    }


    @Override
    public void update( Request request, Developer developer ) {
        List< Object > roles = request.getParameters( DeveloperParameter.ROLES );

        developer.getUser().getRoles().clear();

        for ( Object role : roles ) {
            developer.getUser().addRole( role.toString() );
        }

        historyHandler.update( developer, DeveloperProperty.ROLES );

        developerRepository.persist( developer );
    }
}
