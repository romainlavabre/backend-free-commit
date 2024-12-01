package com.free.commit.controller.admin;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Executor;
import com.free.commit.repository.ExecutorRepository;
import jakarta.transaction.Transactional;
import org.romainlavabre.crud.Create;
import org.romainlavabre.crud.Update;
import org.romainlavabre.database.DataStorageHandler;
import org.romainlavabre.encoder.Encoder;
import org.romainlavabre.request.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController( "AdminExecutorController" )
@RequestMapping( path = "/admin/executors" )
public class ExecutorController {
    protected final Create< Executor > createExecutor;
    protected final Update< Executor > updateExecutorName;
    protected final Update< Executor > updateExecutorVariables;
    protected final ExecutorRepository executorRepository;
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;


    public ExecutorController(
            Create< Executor > createExecutor,
            Update< Executor > updateExecutorName,
            Update< Executor > updateExecutorVariables,
            ExecutorRepository executorRepository,
            DataStorageHandler dataStorageHandler,
            Request request ) {
        this.createExecutor          = createExecutor;
        this.updateExecutorName      = updateExecutorName;
        this.updateExecutorVariables = updateExecutorVariables;
        this.executorRepository      = executorRepository;
        this.dataStorageHandler      = dataStorageHandler;
        this.request                 = request;
    }


    @GetMapping( path = "/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > findOne( @PathVariable long id ) {
        Executor executor = executorRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( executor, GroupType.ADMIN ) );
    }


    @GetMapping
    public ResponseEntity< List< Map< String, Object > > > findAll() {
        List< Executor > executors = executorRepository.findAll();

        return ResponseEntity.ok( Encoder.encode( executors, GroupType.ADMIN ) );
    }


    @Transactional
    @PostMapping
    public ResponseEntity< Map< String, Object > > create() {
        Executor executor = new Executor();

        createExecutor.create( request, executor );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( executor, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/{id:[0-9]+}/name" )
    public ResponseEntity< Map< String, Object > > updateName( @PathVariable long id ) {
        Executor executor = executorRepository.findOrFail( id );

        updateExecutorName.update( request, executor );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( executor, GroupType.ADMIN ) );
    }


    @Transactional
    @PatchMapping( path = "/{id:[0-9]+}/variables" )
    public ResponseEntity< Map< String, Object > > updateVariables( @PathVariable long id ) {
        Executor executor = executorRepository.findOrFail( id );

        updateExecutorVariables.update( request, executor );

        dataStorageHandler.save();

        return ResponseEntity.ok( Encoder.encode( executor, GroupType.ADMIN ) );
    }
}
