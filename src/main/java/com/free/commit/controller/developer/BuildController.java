package com.free.commit.controller.developer;

import com.free.commit.api.json.Encoder;
import com.free.commit.build.BuildManager;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.repository.CredentialRepository;
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


    public BuildController(
            BuildManager buildManager,
            CredentialRepository credentialRepository,
            ProjectRepository projectRepository ) {
        this.buildManager         = buildManager;
        this.credentialRepository = credentialRepository;
        this.projectRepository    = projectRepository;
    }


    @GetMapping( path = "/builds/by/project/{id:[0-9]+}" )
    public ResponseEntity< List< Map< String, Object > > > getAllBuildsByProject( @PathVariable( "id" ) long id ) {
        Project       project = projectRepository.findOrFail( id );
        List< Build > builds  = project.getBuilds();

        return ResponseEntity.ok( Encoder.encode( builds, GroupType.DEVELOPER ) );
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
        Project project = new Project();
        project.setBranch( "cicd/test" )
               .setRepository( "git@github.com:fairfair-cloud/service-emergency.git" )
               .setName( "service-emergency-dev" )
               .setSpecFilePath( "/.free-commit/deploy.yaml" )
               .setRepositoryCredential( credentialRepository.findOrFail( 2L ) );

        BuildManager.Queued queued = buildManager.launch( project );

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( queued ) );
    }
}
