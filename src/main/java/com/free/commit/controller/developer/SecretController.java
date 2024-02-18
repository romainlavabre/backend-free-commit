package com.free.commit.controller.developer;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Secret;
import com.free.commit.repository.SecretRepository;
import org.romainlavabre.encoder.Encoder;
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
@RestController( "DeveloperSecretController" )
@RequestMapping( path = "/developer" )
public class SecretController {

    protected final SecretRepository secretRepository;


    public SecretController( SecretRepository secretRepository ) {
        this.secretRepository = secretRepository;
    }


    @GetMapping( path = "/secrets/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getSecret( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/secrets" )
    public ResponseEntity< List< Map< String, Object > > > getAllSecrets() {
        List< Secret > secrets = secretRepository.findAll();

        return ResponseEntity.ok( Encoder.encode( secrets, GroupType.DEVELOPER ) );
    }
}
