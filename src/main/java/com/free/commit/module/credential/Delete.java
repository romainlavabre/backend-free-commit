package com.free.commit.module.credential;

import org.romainlavabre.history.HistoryHandler;
import com.free.commit.entity.Credential;
import com.free.commit.repository.CredentialRepository;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "deleteCredential" )
public class Delete implements org.romainlavabre.crud.Delete< Credential > {

    protected final CredentialRepository credentialRepository;
    protected final HistoryHandler       historyHandler;


    public Delete(
            CredentialRepository credentialRepository,
            HistoryHandler historyHandler ) {
        this.credentialRepository = credentialRepository;
        this.historyHandler       = historyHandler;
    }


    @Override
    public void delete( Request request, Credential credential ) {
        credentialRepository.remove( credential );

        historyHandler.delete( credential );
    }
}
