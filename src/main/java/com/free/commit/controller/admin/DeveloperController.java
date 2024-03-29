package com.free.commit.controller.admin;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Developer;
import com.free.commit.repository.DeveloperRepository;
import jakarta.transaction.Transactional;
import org.romainlavabre.crud.Create;
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
@RestController( "AdminDeveloperController" )
@RequestMapping( path = "/admin" )
public class DeveloperController {

    protected final Create< Developer > createDeveloper;
    protected final Update< Developer > updateDeveloperEmail;
    protected final Update< Developer > updateDeveloperEnabled;
    protected final Update< Developer > updateDeveloperGithubUsername;
    protected final Update< Developer > updateDeveloperGitlabUsername;
    protected final Update< Developer > updateDeveloperUsername;
    protected final Update< Developer > updateDeveloperPassword;
    protected final Update< Developer > updateDeveloperRole;
    protected final Update< Developer > updateDeveloperProjects;
    protected final DeveloperRepository developerRepository;
    protected final DataStorageHandler  dataStorageHandler;
    protected final Request             request;


    public DeveloperController(
            Create< Developer > createDeveloper,
            Update< Developer > updateDeveloperEmail,
            Update< Developer > updateDeveloperEnabled,
            Update< Developer > updateDeveloperGithubUsername,
            Update< Developer > updateDeveloperGitlabUsername,
            Update< Developer > updateDeveloperUsername,
            Update< Developer > updateDeveloperPassword,
            Update< Developer > updateDeveloperRole,
            Update< Developer > updateDeveloperProjects,
            DeveloperRepository developerRepository,
            DataStorageHandler dataStorageHandler,
            Request request ) {
        this.createDeveloper               = createDeveloper;
        this.updateDeveloperEmail          = updateDeveloperEmail;
        this.updateDeveloperEnabled        = updateDeveloperEnabled;
        this.updateDeveloperGithubUsername = updateDeveloperGithubUsername;
        this.updateDeveloperGitlabUsername = updateDeveloperGitlabUsername;
        this.updateDeveloperUsername       = updateDeveloperUsername;
        this.updateDeveloperPassword       = updateDeveloperPassword;
        this.updateDeveloperRole           = updateDeveloperRole;
        this.updateDeveloperProjects       = updateDeveloperProjects;
        this.developerRepository           = developerRepository;
        this.dataStorageHandler            = dataStorageHandler;
        this.request                       = request;
    }


    @GetMapping( path = "/developers/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getDeveloper( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
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
    public ResponseEntity< Map< String, Object > > updateEmail( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperEmail.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/enabled" )
    public ResponseEntity< Map< String, Object > > updateEnabled( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperEnabled.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/github_username" )
    public ResponseEntity< Map< String, Object > > updateGithubUsername( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperGithubUsername.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/gitlab_username" )
    public ResponseEntity< Map< String, Object > > updateGitlabUsername( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperGitlabUsername.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/username" )
    public ResponseEntity< Map< String, Object > > updateUsername( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperUsername.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/password" )
    public ResponseEntity< Map< String, Object > > updatePassword( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperPassword.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = { "/developers/{id:[0-9]+}/role", "/developers/{id:[0-9]+}/roles" } )
    public ResponseEntity< Map< String, Object > > updateRole( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperRole.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/developers/{id:[0-9]+}/projects" )
    public ResponseEntity< Map< String, Object > > updateProjects( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        updateDeveloperProjects.update( request, developer );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.ADMIN ) );
    }
}
