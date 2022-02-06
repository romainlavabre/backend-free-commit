package com.free.commit.module.project;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectKeepNumberBuild" )
public class UpdateKeepNumberBuild implements Update< Project > {

    protected final ProjectRepository projectRepository;
    protected final HistoryHandler    historyHandler;


    public UpdateKeepNumberBuild(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler ) {
        this.projectRepository = projectRepository;
        this.historyHandler    = historyHandler;
    }


    @Override
    public void update( Request request, Project project ) {
        Integer keepNumberBuild = Cast.getInteger( request.getParameter( ProjectParameter.KEEP_NUMBER_BUILD ) );

        project.setKeepNumberBuild( keepNumberBuild );

        historyHandler.update( project, ProjectProperty.KEEP_NUMBER_BUILD );

        projectRepository.persist( project );
    }
}
