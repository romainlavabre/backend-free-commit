package com.free.commit.controller.developer;

import com.free.commit.api.json.Encoder;
import com.free.commit.api.security.Security;
import com.free.commit.api.security.UserRepository;
import com.free.commit.build.BuildManager;
import com.free.commit.build.Initiator;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.response.Message;
import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Build;
import com.free.commit.entity.Developer;
import com.free.commit.entity.Project;
import com.free.commit.exception.HttpForbiddenException;
import com.free.commit.repository.BuildRepository;
import com.free.commit.repository.CredentialRepository;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "DeveloperBuildController" )
@RequestMapping( path = "/developer" )
public class BuildController {

    protected final BuildManager         buildManager;
    protected final CredentialRepository credentialRepository;
    protected final ProjectRepository    projectRepository;
    protected final BuildRepository      buildRepository;
    protected final DeveloperRepository  developerRepository;
    protected final UserRepository       userRepository;
    protected final Security             security;


    public BuildController(
            BuildManager buildManager,
            CredentialRepository credentialRepository,
            ProjectRepository projectRepository,
            BuildRepository buildRepository,
            DeveloperRepository developerRepository,
            UserRepository userRepository,
            Security security ) {
        this.buildManager         = buildManager;
        this.credentialRepository = credentialRepository;
        this.projectRepository    = projectRepository;
        this.buildRepository      = buildRepository;
        this.developerRepository  = developerRepository;
        this.userRepository       = userRepository;
        this.security             = security;
    }


    @GetMapping( path = "/builds/by/project/{id:[0-9]+}" )
    public ResponseEntity< List< Map< String, Object > > > getAllBuildsByProject( @PathVariable( "id" ) long id ) {
        Project       project = projectRepository.findOrFail( id );
        List< Build > builds  = project.getBuilds();

        return ResponseEntity.ok( Encoder.encode( builds, GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/builds/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getBuild( @PathVariable( "id" ) long id ) {
        Build build = buildRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( build, GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/builds/output/{executorId}" )
    public ResponseEntity< String > getOutput( @PathVariable( "executorId" ) String id ) {
        return ResponseEntity.ok( buildManager.getLogs( id ) );
    }


    @GetMapping( path = "/builds/queueds" )
    public ResponseEntity< List< Map< String, Object > > > getQueueds() {
        return ResponseEntity.ok( Encoder.encode( buildManager.getQueueds() ) );
    }


    @GetMapping( path = "/builds/executeds" )
    public ResponseEntity< List< Map< String, Object > > > getExecuteds() {
        return ResponseEntity.ok( Encoder.encode( buildManager.getExecuteds() ) );
    }


    @PostMapping( path = "/builds/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > build( @PathVariable( "id" ) long id ) {
        Project   project        = projectRepository.findOrFail( id );
        Developer foundDeveloper = null;

        if ( !security.hasRole( Role.ADMIN ) ) {
            boolean isAllowed = false;

            for ( Developer developer : project.getDevelopers() ) {
                if ( developer.getUser().getUsername().equals( security.getUsername() ) ) {
                    foundDeveloper = developer;
                    isAllowed      = true;
                    break;
                }
            }

            if ( !isAllowed ) {
                throw new HttpForbiddenException( Message.LAUNCH_BUILD_NOT_ALLOWED );
            }
        } else {
            foundDeveloper = developerRepository.findOrFailByUser( userRepository.findByUsername( security.getUsername() ) );
        }

        BuildManager.Queued queued = buildManager.launch( project, new Initiator(
                foundDeveloper.getEmail(),
                true
        ), "" );

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( queued ) );
    }


    @DeleteMapping( path = "/builds/kill/executed/{executorId}" )
    public ResponseEntity< Map< String, Object > > killExecuted( @PathVariable( "executorId" ) String executorId ) {
        buildManager.killExecuted( executorId );

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping( path = "/builds/kill/queued/{executorId}" )
    public ResponseEntity< Map< String, Object > > buildQueued( @PathVariable( "executorId" ) String executorId ) {
        buildManager.killQueued( executorId );

        return ResponseEntity.noContent().build();
    }
}
