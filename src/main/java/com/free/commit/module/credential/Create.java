package com.free.commit.module.credential;

import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Credential;
import com.free.commit.parameter.CredentialParameter;
import com.free.commit.repository.CredentialRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createCredential" )
public class Create implements com.free.commit.api.crud.Create< Credential > {

    protected final CredentialRepository credentialRepository;
    protected final HistoryHandler       historyHandler;


    public Create(
            CredentialRepository credentialRepository,
            HistoryHandler historyHandler ) {
        this.credentialRepository = credentialRepository;
        this.historyHandler       = historyHandler;
    }


    @Override
    public void create( Request request, Credential credential ) {
        String name   = ( String ) request.getParameter( CredentialParameter.NAME );
        String sshKey = ( String ) request.getParameter( CredentialParameter.SSH_KEY );

        credential.setName( name )
                  .setSshKey( sshKey );

        historyHandler.create( credential );

        credentialRepository.persist( credential );
    }
}
