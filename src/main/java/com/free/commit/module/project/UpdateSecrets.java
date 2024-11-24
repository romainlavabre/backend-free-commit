package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectDevelopers" )
public class UpdateSecrets implements Update< Project > {

    protected final ProjectRepository   projectRepository;
    protected final HistoryHandler      historyHandler;
    protected final DeveloperRepository developerRepository;


    public UpdateSecrets(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler,
            DeveloperRepository developerRepository ) {
        this.projectRepository   = projectRepository;
        this.historyHandler      = historyHandler;
        this.developerRepository = developerRepository;
    }


    @Override
    public void update( Request request, Project project ) {
        List< Object > developersId = request.getParameters( ProjectParameter.DEVELOPERS );

        project.getDevelopers().clear();

        for ( Object developerId : developersId ) {
            project.addDeveloper( developerRepository.findOrFail( Cast.getLong( developerId ) ) );
        }

        historyHandler.update( project, ProjectProperty.DEVELOPERS );

        projectRepository.persist( project );
    }
}
