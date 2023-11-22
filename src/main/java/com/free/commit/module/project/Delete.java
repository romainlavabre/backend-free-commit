package com.free.commit.module.project;

import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.repository.ProjectRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "deleteProject" )
public class Delete implements com.free.commit.api.crud.Delete< Project > {

    protected final ProjectRepository projectRepository;
    protected final HistoryHandler    historyHandler;


    public Delete(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler ) {
        this.projectRepository = projectRepository;
        this.historyHandler    = historyHandler;
    }


    @Override
    public void delete( Request request, Project project ) {
        for ( Secret secret : project.getSecrets() ) {
            secret.removeProject( project );
        }

        project.getSecrets().clear();

        projectRepository.remove( project );

        historyHandler.delete( project );
    }
}
