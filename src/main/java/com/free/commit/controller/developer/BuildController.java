package com.free.commit.controller.developer;

import com.free.commit.build.BuildManager;
import com.free.commit.entity.Project;
import com.free.commit.repository.CredentialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "DeveloperBuildController" )
@RequestMapping( path = "/developer" )
public class BuildController {

    protected final BuildManager         buildManager;
    protected final CredentialRepository credentialRepository;


    public BuildController(
            BuildManager buildManager,
            CredentialRepository credentialRepository ) {
        this.buildManager         = buildManager;
        this.credentialRepository = credentialRepository;
    }


    @GetMapping( path = "/builds/output/{executorId}" )
    public ResponseEntity< String > getOutput( @PathVariable( "executorId" ) String id ) {
        return ResponseEntity.ok( buildManager.getLogs( id ) );
    }


    @PostMapping( path = "/builds/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > build( @PathVariable( "id" ) long id ) {
        Project project = new Project();
        project.setBranch( "cicd/test" )
               .setRepository( "git@github.com:fairfair-cloud/service-emergency.git" )
               .setName( "service-emergency-dev" )
               .setSpecFilePath( "/.free-commit/deploy.yaml" )
               .setRepositoryCredential( credentialRepository.findOrFail( 2L ) );

        String executorId = buildManager.launch( project );

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Map.of(
                        "executor_id", executorId
                ) );
    }
}
