package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectAllowConcurrentExecution" )
public class UpdateAllowConcurrentExecution implements Update< Project > {

    protected final ProjectRepository projectRepository;
    protected final HistoryHandler    historyHandler;


    public UpdateAllowConcurrentExecution(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler ) {
        this.projectRepository = projectRepository;
        this.historyHandler    = historyHandler;
    }


    @Override
    public void update( Request request, Project project ) {
        Boolean allowConcurrentExecution = Cast.getBoolean( request.getParameter( ProjectParameter.ALLOW_CONCURRENT_EXECUTION ) );

        project.setAllowConcurrentExecution( allowConcurrentExecution );

        historyHandler.update( project, ProjectProperty.ALLOW_CONCURRENT_EXECUTION );

        projectRepository.persist( project );
    }
}
