package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.repository.ExecutorRepository;
import com.free.commit.repository.ProjectRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectExecutor" )
public class UpdateExecutor implements Update< Project > {

    protected final ProjectRepository  projectRepository;
    protected final HistoryHandler     historyHandler;
    protected final ExecutorRepository executorRepository;


    public UpdateExecutor( ProjectRepository projectRepository, HistoryHandler historyHandler, ExecutorRepository executorRepository ) {
        this.projectRepository  = projectRepository;
        this.historyHandler     = historyHandler;
        this.executorRepository = executorRepository;
    }


    @Override
    public void update( Request request, Project project ) {
        Long executorId = request.getParameter( ProjectParameter.EXECUTOR, Long.class );

        project.setExecutor( executorRepository.findOrFail( executorId ) );

        projectRepository.persist( project );
    }
}
