package com.free.commit.module.developer;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperProjects" )
public class UpdateProjects implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;
    protected final ProjectRepository   projectRepository;


    public UpdateProjects(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
        this.projectRepository   = projectRepository;
    }


    @Override
    public void update( Request request, Developer developer ) {
        List< Object > projectsId = request.getParameters( DeveloperParameter.PROJECTS_ID );

        developer.cleaProjects();

        for ( Object projectId : projectsId ) {
            developer.addProject( projectRepository.findOrFail( Cast.getLong( projectId ) ) );
        }

        historyHandler.update( developer, DeveloperProperty.PROJECTS );

        developerRepository.persist( developer );
    }
}
