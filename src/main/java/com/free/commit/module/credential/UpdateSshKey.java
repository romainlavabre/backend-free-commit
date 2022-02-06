package com.free.commit.module.credential;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Credential;
import com.free.commit.parameter.CredentialParameter;
import com.free.commit.property.CredentialProperty;
import com.free.commit.repository.CredentialRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateCredentialSshKey" )
public class UpdateSshKey implements Update< Credential > {

    protected final CredentialRepository credentialRepository;
    protected final HistoryHandler       historyHandler;


    public UpdateSshKey(
            CredentialRepository credentialRepository,
            HistoryHandler historyHandler ) {
        this.credentialRepository = credentialRepository;
        this.historyHandler       = historyHandler;
    }


    @Override
    public void update( Request request, Credential credential ) {
        String sshKey = ( String ) request.getParameter( CredentialParameter.SSH_KEY );

        credential.setSshKey( sshKey );

        historyHandler.update( credential, CredentialProperty.SSH_KEY );

        credentialRepository.persist( credential );
    }
}
