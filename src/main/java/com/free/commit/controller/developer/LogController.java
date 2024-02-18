package com.free.commit.controller.developer;

import com.free.commit.build.BuildManager;
import com.free.commit.configuration.json.GroupType;
import org.romainlavabre.encoder.Encoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping( path = "/developer/logs" )
public class LogController {
    protected final BuildManager buildManager;


    public LogController( BuildManager buildManager ) {
        this.buildManager = buildManager;
    }


    @GetMapping( path = "/{executorId}/{step}/{lineNumber}" )
    public ResponseEntity< Map< String, Object > > getOutput( @PathVariable( "executorId" ) String id, @PathVariable( "step" ) String step, @PathVariable( "lineNumber" ) int lineNumber ) {
        return ResponseEntity.ok( Encoder.encode( buildManager.getLog( id, step, lineNumber ), GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/{buildId}/{step}" )
    public ResponseEntity< Map< String, Object > > getOutput( @PathVariable( "buildId" ) long id, @PathVariable( "step" ) String step ) {
        return ResponseEntity.ok( Encoder.encode( buildManager.getLog( id, step ), GroupType.DEVELOPER ) );
    }
}
