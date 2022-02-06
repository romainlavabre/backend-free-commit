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
@Service( "updateCredentialName" )
public class UpdateName implements Update< Credential > {

    protected final CredentialRepository credentialRepository;
    protected final HistoryHandler       historyHandler;


    public UpdateName(
            CredentialRepository credentialRepository,
            HistoryHandler historyHandler ) {
        this.credentialRepository = credentialRepository;
        this.historyHandler       = historyHandler;
    }


    @Override
    public void update( Request request, Credential credential ) {
        String name = ( String ) request.getParameter( CredentialParameter.NAME );

        credential.setName( name );

        historyHandler.update( credential, CredentialProperty.NAME );

        credentialRepository.persist( credential );
    }
}
