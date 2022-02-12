package com.free.commit.module.secret;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.property.SecretProperty;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateSecretProject" )
public class UpdateProject implements Update< Secret > {

    protected final SecretRepository  secretRepository;
    protected final HistoryHandler    historyHandler;
    protected final ProjectRepository projectRepository;


    public UpdateProject(
            SecretRepository secretRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.secretRepository  = secretRepository;
        this.historyHandler    = historyHandler;
        this.projectRepository = projectRepository;
    }


    @Override
    public void update( Request request, Secret secret ) {
        Long projectId = Cast.getLong( request.getParameter( SecretParameter.PROJECT ) );

        if ( projectId != null ) {
            Project project = projectRepository.findOrFail( projectId );

            secret.setProject( project );
        } else {
            secret.setProject( null );
        }

        historyHandler.update( secret, SecretProperty.PROJECT );

        secretRepository.persist( secret );
    }
}
