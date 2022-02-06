package com.free.commit.controller.developer;

import com.free.commit.api.json.Encoder;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Credential;
import com.free.commit.repository.CredentialRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "DeveloperCredentialController" )
@RequestMapping( path = "/developer" )
public class CredentialController {
    protected final CredentialRepository credentialRepository;


    public CredentialController( CredentialRepository credentialRepository ) {
        this.credentialRepository = credentialRepository;
    }


    @GetMapping( path = "/credentials/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getCredential( @PathVariable( "id" ) long id ) {
        Credential credential = credentialRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( credential, GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/credentials" )
    public ResponseEntity< List< Map< String, Object > > > getCredential() {
        List< Credential > credentials = credentialRepository.findAll();

        return ResponseEntity.ok( Encoder.encode( credentials, GroupType.DEVELOPER ) );
    }
}
