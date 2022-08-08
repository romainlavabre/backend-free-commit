package com.free.commit.module.secret;

import com.free.commit.api.crud.Update;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.repository.SecretRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateSecretEscapeChar" )
public class UpdateEscapeChar implements Update< Secret > {

    protected final SecretRepository secretRepository;


    public UpdateEscapeChar(
            SecretRepository secretRepository ) {
        this.secretRepository = secretRepository;
    }


    @Override
    public void update( Request request, Secret secret ) {
        String escapeChar = ( String ) request.getParameter( SecretParameter.ESCAPE_CHAR );

        secret.setEscapeChar( escapeChar.replaceAll( ",$", "" ).replaceAll( "^,", "" ) );

        secretRepository.persist( secret );
    }
}
