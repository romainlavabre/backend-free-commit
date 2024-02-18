package com.free.commit.module.secret;

import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createSecret" )
public class Create implements org.romainlavabre.crud.Create< Secret > {

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
        String         name       = request.getParameter( SecretParameter.NAME, String.class );
        String         value      = request.getParameter( SecretParameter.VALUE, String.class );
        String         env        = request.getParameter( SecretParameter.ENV, String.class );
        List< Object > projectsId = request.getParameters( SecretParameter.PROJECTS );

        secret
                .setName( name )
                .setValue( value )
                .setEnv( env );

        if ( projectsId != null ) {
            for ( Object object : projectsId ) {
                secret.addProject( projectRepository.findOrFail( Cast.getLong( object ) ) );
            }
        }

        historyHandler.create( secret );

        secretRepository.persist( secret );
    }
}
