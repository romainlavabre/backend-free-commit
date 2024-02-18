package com.free.commit.module.secret;

import com.free.commit.entity.Secret;
import com.free.commit.repository.SecretRepository;
import org.romainlavabre.crud.Delete;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
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
