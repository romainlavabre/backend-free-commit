package com.free.commit.controller.admin;

import com.free.commit.api.crud.Create;
import com.free.commit.api.crud.Delete;
import com.free.commit.api.crud.Update;
import com.free.commit.api.json.Encoder;
import com.free.commit.api.request.Request;
import com.free.commit.api.storage.data.DataStorageHandler;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Secret;
import com.free.commit.repository.SecretRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "AdminSecretController" )
@RequestMapping( path = "/admin" )
public class SecretController {

    protected final Create< Secret >   createSecret;
    protected final Update< Secret >   updateSecretName;
    protected final Update< Secret >   updateSecretValue;
    protected final Update< Secret >   updateSecretEscapeChar;
    protected final Update< Secret >   updateSecretProjects;
    protected final Delete< Secret >   deleteSecret;
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;
    protected final SecretRepository   secretRepository;


    public SecretController(
            Create< Secret > createSecret,
            Update< Secret > updateSecretName,
            Update< Secret > updateSecretValue,
            Update< Secret > updateSecretEscapeChar,
            Update< Secret > updateSecretProjects,
            Delete< Secret > deleteSecret,
            DataStorageHandler dataStorageHandler,
            Request request,
            SecretRepository secretRepository ) {
        this.createSecret           = createSecret;
        this.updateSecretName       = updateSecretName;
        this.updateSecretValue      = updateSecretValue;
        this.updateSecretEscapeChar = updateSecretEscapeChar;
        this.updateSecretProjects   = updateSecretProjects;
        this.deleteSecret           = deleteSecret;
        this.dataStorageHandler     = dataStorageHandler;
        this.request                = request;
        this.secretRepository       = secretRepository;
    }


    @GetMapping( path = "/secrets/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getSecret( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @PostMapping( path = "/secrets" )
    public ResponseEntity< Map< String, Object > > create() {
        Secret secret = new Secret();

        createSecret.create( request, secret );

        dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/secrets/{id:[0-9]+}/name" )
    public ResponseEntity< Map< String, Object > > updateName( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        updateSecretName.update( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/secrets/{id:[0-9]+}/value" )
    public ResponseEntity< Map< String, Object > > updateValue( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        updateSecretValue.update( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/secrets/{id:[0-9]+}/escape_char" )
    public ResponseEntity< Map< String, Object > > updateEscapeChar( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        updateSecretEscapeChar.update( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/secrets/{id:[0-9]+}/projects" )
    public ResponseEntity< Map< String, Object > > updateProject( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        updateSecretProjects.update( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @DeleteMapping( path = "/secrets/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > delete( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        deleteSecret.delete( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }
}
