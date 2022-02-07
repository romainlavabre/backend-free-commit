package com.free.commit.module.project;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectDevelopers" )
public class UpdateDevelopers implements Update< Project > {

    protected final ProjectRepository   projectRepository;
    protected final HistoryHandler      historyHandler;
    protected final DeveloperRepository developerRepository;


    public UpdateDevelopers(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler,
            DeveloperRepository developerRepository ) {
        this.projectRepository   = projectRepository;
        this.historyHandler      = historyHandler;
        this.developerRepository = developerRepository;
    }


    @Override
    public void update( Request request, Project project ) {
        List< Object > developersId = request.getParameters( ProjectParameter.DEVELOPERS_ID );

        project.getDevelopers().clear();
        
        for ( Object developerId : developersId ) {
            project.addDeveloper( developerRepository.findOrFail( Cast.getLong( developerId ) ) );
        }

        historyHandler.update( project, ProjectProperty.DEVELOPERS );

        projectRepository.persist( project );
    }
}
