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
@Service( "updateSecretValue" )
public class UpdateValue implements Update< Secret > {

    protected final SecretRepository secretRepository;
    protected final HistoryHandler   historyHandler;


    public UpdateValue(
            SecretRepository secretRepository,
            HistoryHandler historyHandler ) {
        this.secretRepository = secretRepository;
        this.historyHandler   = historyHandler;
    }


    @Override
    public void update( Request request, Secret secret ) {
        String value = ( String ) request.getParameter( SecretParameter.VALUE );

        secret.setValue( value );

        historyHandler.update( secret, SecretProperty.VALUE );

        secretRepository.persist( secret );
    }
}
