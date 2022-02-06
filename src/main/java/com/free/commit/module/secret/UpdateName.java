package com.free.commit.module.secret;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.property.SecretProperty;
import com.free.commit.repository.SecretRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateSecretName" )
public class UpdateName implements Update< Secret > {

    protected final SecretRepository secretRepository;
    protected final HistoryHandler   historyHandler;


    public UpdateName(
            SecretRepository secretRepository,
            HistoryHandler historyHandler ) {
        this.secretRepository = secretRepository;
        this.historyHandler   = historyHandler;
    }


    @Override
    public void update( Request request, Secret secret ) {
        String name = ( String ) request.getParameter( SecretParameter.NAME );

        secret.setName( name );

        historyHandler.update( secret, SecretProperty.NAME );

        secretRepository.persist( secret );
    }
}
