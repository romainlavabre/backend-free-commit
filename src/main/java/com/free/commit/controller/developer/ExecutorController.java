package com.free.commit.controller.developer;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Executor;
import com.free.commit.repository.ExecutorRepository;
import org.romainlavabre.encoder.Encoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController( "DeveloperExecutorController" )
@RequestMapping( path = "/developer/executors" )
public class ExecutorController {
    protected final ExecutorRepository executorRepository;


    public ExecutorController( ExecutorRepository executorRepository ) {
        this.executorRepository = executorRepository;
    }


    @GetMapping
    public ResponseEntity< List< Map< String, Object > > > findAll() {
        List< Executor > executors = executorRepository.findAll();

        return ResponseEntity.ok( Encoder.encode( executors, GroupType.DEVELOPER ) );
    }
}
