package com.free.commit.controller.admin;

import com.free.commit.api.crud.Create;
import com.free.commit.api.crud.Update;
import com.free.commit.api.json.Encoder;
import com.free.commit.api.request.Request;
import com.free.commit.api.storage.data.DataStorageHandler;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Developer;
import com.free.commit.repository.DeveloperRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "AdminDeveloperController" )
@RequestMapping( path = "/admin" )
public class DeveloperController {

    protected final Create< Developer > createDeveloper;
    protected final Update< Developer > updateDeveloperEmail;
    protected final Update< Developer > updateDeveloperEnabled;
    protected final Update< Developer > updateDeveloperGithubUsername;
    protected final Update< Developer > updateDeveloperUsername;
    protected final Update< Developer > updateDeveloperPassword;
    protected final DeveloperRepository developerRepository;
    protected final DataStorageHandler  dataStorageHandler;
    protected final Request             request;


    public DeveloperController(
            Create< Developer > createDeveloper,
            Update< Developer > updateDeveloperEmail,
            Update< Developer > updateDeveloperEnabled,
            Update< Developer > updateDeveloperGithubUsername,
            Update< Developer > updateDeveloperUsername,
            Update< Developer > updateDeveloperPassword,
            DeveloperRepository developerRepository,
            DataStorageHandler dataStorageHandler,
            Request request ) {
        this.createDeveloper               = createDeveloper;
        this.updateDeveloperEmail          = updateDeveloperEmail;
        this.updateDeveloperEnabled        = updateDeveloperEnabled;
        this.updateDeveloperGithubUsername = updateDeveloperGithubUsername;
        this.updateDeveloperUsername       = updateDeveloperUsername;
        this.updateDeveloperPassword       = updateDeveloperPassword;
        this.developerRepository           = developerRepository;
        this.dataStorageHandler            = dataStorageHandler;
        this.request                       = request;
    }


    @Transactional
    @PostMapping( path = "/developers" )
    public ResponseEntity< Map< String, Object > > create() {
        Developer developer = new Developer();

        createDeveloper.create( request, developer );

        dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/email" )
    public ResponseEntity< Void > updateEmail( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperEmail.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/enabled" )
    public ResponseEntity< Void > updateEnabled( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperEnabled.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/github_username" )
    public ResponseEntity< Void > updateGithubUsername( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperGithubUsername.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/username" )
    public ResponseEntity< Void > updateUsername( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperUsername.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/password" )
    public ResponseEntity< Void > updatePassword( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperPassword.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }
}
