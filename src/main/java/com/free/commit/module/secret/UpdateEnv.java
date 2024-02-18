package com.free.commit.module.secret;

import com.free.commit.entity.Secret;
import com.free.commit.parameter.SecretParameter;
import com.free.commit.repository.SecretRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateSecretEnv" )
public class UpdateEnv implements Update< Secret > {

    protected final SecretRepository secretRepository;


    public UpdateEnv(
            SecretRepository secretRepository ) {
        this.secretRepository = secretRepository;
    }


    @Override
    public void update( Request request, Secret secret ) {
        String env = request.getParameter( SecretParameter.ENV, String.class );

        secret.setEnv( env );

        secretRepository.persist( secret );
    }
}
