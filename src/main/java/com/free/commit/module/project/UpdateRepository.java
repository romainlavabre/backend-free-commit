package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.ProjectRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectRepository" )
public class UpdateRepository implements Update< Project > {

    protected final ProjectRepository projectRepository;
    protected final HistoryHandler    historyHandler;


    public UpdateRepository(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler ) {
        this.projectRepository = projectRepository;
        this.historyHandler    = historyHandler;
    }


    @Override
    public void update( Request request, Project project ) {
        String repository = ( String ) request.getParameter( ProjectParameter.REPOSITORY );

        project.setRepository( repository );

        historyHandler.update( project, ProjectProperty.REPOSITORY );

        projectRepository.persist( project );
    }
}
