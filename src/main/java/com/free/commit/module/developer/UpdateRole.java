package com.free.commit.module.developer;

import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperRole" )
public class UpdateRole implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;
    protected final ProjectRepository   projectRepository;


    public UpdateRole(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
        this.projectRepository   = projectRepository;
    }


    @Override
    public void update( Request request, Developer developer ) {
        List< Object > roles = request.getParameters( DeveloperParameter.ROLE );

        developer.getUser().getRoles().clear();

        for ( Object role : roles ) {
            developer.getUser().addRole( role.toString() );
        }

        historyHandler.update( developer, DeveloperProperty.ROLES );

        developerRepository.persist( developer );
    }
}
