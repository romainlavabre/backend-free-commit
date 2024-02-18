package com.free.commit.controller.admin;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Credential;
import com.free.commit.repository.CredentialRepository;
import jakarta.transaction.Transactional;
import org.romainlavabre.crud.Create;
import org.romainlavabre.crud.Delete;
import org.romainlavabre.crud.Update;
import org.romainlavabre.database.DataStorageHandler;
import org.romainlavabre.encoder.Encoder;
import org.romainlavabre.request.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "AdminCredentialController" )
@RequestMapping( path = "/admin" )
public class CredentialController {

    protected final Create< Credential > createCredential;
    protected final Update< Credential > updateCredentialName;
    protected final Update< Credential > updateCredentialSshKey;
    protected final Delete< Credential > deleteCredential;
    protected final CredentialRepository credentialRepository;
    protected final DataStorageHandler   dataStorageHandler;
    protected final Request              request;


    public CredentialController(
            Create< Credential > createCredential,
            Update< Credential > updateCredentialName,
            Update< Credential > updateCredentialSshKey,
            Delete< Credential > deleteCredential,
            CredentialRepository credentialRepository,
            DataStorageHandler dataStorageHandler,
            Request request ) {
        this.createCredential       = createCredential;
        this.updateCredentialName   = updateCredentialName;
        this.updateCredentialSshKey = updateCredentialSshKey;
        this.deleteCredential       = deleteCredential;
        this.credentialRepository   = credentialRepository;
        this.dataStorageHandler     = dataStorageHandler;
        this.request                = request;
    }


    @GetMapping( path = "/credentials/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getCredential( @PathVariable( "id" ) long id ) {
        Credential credential = credentialRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( credential, GroupType.ADMIN ) );
    }


    @Transactional
    @PostMapping( path = "/credentials" )
    public ResponseEntity< Map< String, Object > > create() {
        Credential credential = new Credential();

        createCredential.create( request, credential );

        dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( credential, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/credentials/{id:[0-9]+}/name" )
    public ResponseEntity< Map< String, Object > > updateName( @PathVariable( "id" ) long id ) {
        Credential credential = credentialRepository.findOrFail( id );

        updateCredentialName.update( request, credential );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( credential, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/credentials/{id:[0-9]+}/ssh_key" )
    public ResponseEntity< Map< String, Object > > updateSshKey( @PathVariable( "id" ) long id ) {
        Credential credential = credentialRepository.findOrFail( id );

        updateCredentialSshKey.update( request, credential );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( credential, GroupType.ADMIN ) );
    }


    @Transactional
    @DeleteMapping( path = "/credentials/{id:[0-9]+}" )
    public ResponseEntity< Void > delete( @PathVariable( "id" ) long id ) {
        Credential credential = credentialRepository.findOrFail( id );

        deleteCredential.delete( request, credential );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }
}
