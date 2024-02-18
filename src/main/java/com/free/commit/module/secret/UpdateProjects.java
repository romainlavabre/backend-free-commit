package com.free.commit.module.secret;

import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.property.SecretProperty;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateSecretProjects" )
public class UpdateProjects implements Update< Secret > {

    protected final SecretRepository  secretRepository;
    protected final HistoryHandler    historyHandler;
    protected final ProjectRepository projectRepository;


    public UpdateProjects(
            SecretRepository secretRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.secretRepository  = secretRepository;
        this.historyHandler    = historyHandler;
        this.projectRepository = projectRepository;
    }


    @Override
    public void update( Request request, Secret secret ) {
        List< Object > projectsId = request.getParameters( SecretParameter.PROJECTS );

        secret.getProjects().clear();

        if ( projectsId != null ) {
            for ( Object object : projectsId ) {
                secret.addProject( projectRepository.findOrFail( Cast.getLong( object ) ) );
            }
        }

        historyHandler.update( secret, SecretProperty.PROJECTS );

        secretRepository.persist( secret );
    }
}
