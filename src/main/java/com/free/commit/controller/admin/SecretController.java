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
    protected final Delete< Secret >   deleteSecret;
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;
    protected final SecretRepository   secretRepository;


    public SecretController(
            Create< Secret > createSecret,
            Update< Secret > updateSecretName,
            Update< Secret > updateSecretValue,
            Delete< Secret > deleteSecret,
            DataStorageHandler dataStorageHandler,
            Request request,
            SecretRepository secretRepository ) {
        this.createSecret       = createSecret;
        this.updateSecretName   = updateSecretName;
        this.updateSecretValue  = updateSecretValue;
        this.deleteSecret       = deleteSecret;
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
        this.secretRepository   = secretRepository;
    }


    @Transactional
    @PostMapping( path = "/secrets" )
    public ResponseEntity< Map< String, Object > > create() {
        Secret secret = new Secret();

        createSecret.create( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( secret, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/secrets/{id:[0-9]+}/name" )
    public ResponseEntity< Void > updateName( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        updateSecretName.update( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    @PatchMapping( path = "/secrets/{id:[0-9]+}/value" )
    public ResponseEntity< Void > updateValue( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        updateSecretValue.update( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    @DeleteMapping( path = "/secrets/{id:[0-9]+}" )
    public ResponseEntity< Void > delete( @PathVariable( "id" ) long id ) {
        Secret secret = secretRepository.findOrFail( id );

        deleteSecret.delete( request, secret );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }
}
