package com.free.commit.module.secret;

import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createSecret" )
public class Create implements com.free.commit.api.crud.Create< Secret > {

    protected final SecretRepository  secretRepository;
    protected final HistoryHandler    historyHandler;
    protected final ProjectRepository projectRepository;


    public Create(
            SecretRepository secretRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.secretRepository  = secretRepository;
        this.historyHandler    = historyHandler;
        this.projectRepository = projectRepository;
    }


    @Override
    public void create( Request request, Secret secret ) {
        String name      = ( String ) request.getParameter( SecretParameter.NAME );
        String value     = ( String ) request.getParameter( SecretParameter.VALUE );
        Long   projectId = Cast.getLong( request.getParameter( SecretParameter.PROJECT ) );

        secret.setName( name )
              .setValue( value );

        if ( projectId != null ) {
            secret.setProject( projectRepository.findOrFail( projectId ) );
        }

        historyHandler.create( secret );

        secretRepository.persist( secret );
    }
}
