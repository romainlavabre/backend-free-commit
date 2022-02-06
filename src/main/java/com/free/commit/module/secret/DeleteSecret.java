package com.free.commit.module.secret;

import com.free.commit.api.crud.Delete;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Secret;
import com.free.commit.repository.SecretRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "deleteSecret" )
public class DeleteSecret implements Delete< Secret > {

    protected final SecretRepository secretRepository;
    protected final HistoryHandler   historyHandler;


    public DeleteSecret(
            SecretRepository secretRepository,
            HistoryHandler historyHandler ) {
        this.secretRepository = secretRepository;
        this.historyHandler   = historyHandler;
    }


    @Override
    public void delete( Request request, Secret secret ) {
        secretRepository.remove( secret );

        historyHandler.delete( secret );
    }
}
