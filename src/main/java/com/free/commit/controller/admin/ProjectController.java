package com.free.commit.controller.admin;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Project;
import com.free.commit.repository.ProjectRepository;
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
@RestController( "AdminProjectController" )
@RequestMapping( path = "/admin" )
public class ProjectController {
    protected final Create< Project >  createProject;
    protected final Update< Project >  updateProjectName;
    protected final Update< Project >  updateProjectDescription;
    protected final Update< Project >  updateProjectRepository;
    protected final Update< Project >  updateProjectBranch;
    protected final Update< Project >  updateProjectSpecFilePath;
    protected final Update< Project >  updateProjectKeepNumberBuild;
    protected final Update< Project >  updateProjectAllowConcurrentExecution;
    protected final Update< Project >  updateProjectDevelopers;
    protected final Update< Project >  updateProjectRepositoryCredential;
    protected final Update< Project >  updateProjectSignatureKey;
    protected final Update< Project >  updateProjectSecrets;
    protected final Delete< Project >  deleteProject;
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;
    protected final ProjectRepository  projectRepository;


    public ProjectController(
            Create< Project > createProject,
            Update< Project > updateProjectName,
            Update< Project > updateProjectDescription,
            Update< Project > updateProjectRepository,
            Update< Project > updateProjectBranch,
            Update< Project > updateProjectSpecFilePath,
            Update< Project > updateProjectKeepNumberBuild,
            Update< Project > updateProjectAllowConcurrentExecution,
            Update< Project > updateProjectDevelopers,
            Update< Project > updateProjectRepositoryCredential,
            Update< Project > updateProjectSignatureKey,
            Update< Project > updateProjectSecrets,
            Delete< Project > deleteProject,
            DataStorageHandler dataStorageHandler,
            Request request,
            ProjectRepository projectRepository ) {
        this.createProject                         = createProject;
        this.updateProjectName                     = updateProjectName;
        this.updateProjectDescription              = updateProjectDescription;
        this.updateProjectRepository               = updateProjectRepository;
        this.updateProjectBranch                   = updateProjectBranch;
        this.updateProjectSpecFilePath             = updateProjectSpecFilePath;
        this.updateProjectKeepNumberBuild          = updateProjectKeepNumberBuild;
        this.updateProjectAllowConcurrentExecution = updateProjectAllowConcurrentExecution;
        this.updateProjectDevelopers               = updateProjectDevelopers;
        this.updateProjectRepositoryCredential     = updateProjectRepositoryCredential;
        this.updateProjectSignatureKey             = updateProjectSignatureKey;
        this.updateProjectSecrets                  = updateProjectSecrets;
        this.deleteProject                         = deleteProject;
        this.dataStorageHandler                    = dataStorageHandler;
        this.request                               = request;
        this.projectRepository                     = projectRepository;
    }


    @GetMapping( path = "/projects/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getProject( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PostMapping( path = "/projects" )
    public ResponseEntity< Map< String, Object > > create() {
        Project project = new Project();

        createProject.create( request, project );

        dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/name" )
    public ResponseEntity< Map< String, Object > > updateName( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectName.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/description" )
    public ResponseEntity< Map< String, Object > > updateDescription( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectDescription.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/repository" )
    public ResponseEntity< Map< String, Object > > updateRepository( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectRepository.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/branch" )
    public ResponseEntity< Map< String, Object > > updateBranch( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectBranch.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/spec_file_path" )
    public ResponseEntity< Map< String, Object > > updateSpecFilePath( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectSpecFilePath.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/keep_number_build" )
    public ResponseEntity< Map< String, Object > > updateKeepNumberBuild( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectKeepNumberBuild.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/allow_concurrent_execution" )
    public ResponseEntity< Map< String, Object > > updateAllowConcurrentExecution( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectAllowConcurrentExecution.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/developers" )
    public ResponseEntity< Map< String, Object > > updateDevelopers( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectDevelopers.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/repository_credential" )
    public ResponseEntity< Map< String, Object > > updateRepositoryCredential( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectRepositoryCredential.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/signature_key" )
    public ResponseEntity< Map< String, Object > > updateSignatureKey( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectSignatureKey.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/projects/{id:[0-9]+}/secrets" )
    public ResponseEntity< Map< String, Object > > updateSecrets( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        updateProjectSecrets.update( request, project );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( project, GroupType.ADMIN ) );
    }


    @Transactional
    @DeleteMapping( path = "/projects/{id:[0-9]+}" )
    public ResponseEntity< Void > delete( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        deleteProject.delete( request, project );

        dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }
}
