package com.free.commit.controller.developer;

import com.free.commit.api.json.Encoder;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Developer;
import com.free.commit.repository.DeveloperRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "DeveloperDeveloperController" )
@RequestMapping( path = "/developer" )
public class DeveloperController {

    protected final DeveloperRepository developerRepository;


    public DeveloperController( DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    @GetMapping( path = "/developers/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getDeveloper( @PathVariable( "id" ) long id ) {
        Developer developer = developerRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( developer, GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/developers" )
    public ResponseEntity< List< Map< String, Object > > > getAllDevelopers() {
        List< Developer > developers = developerRepository.findAll();

        return ResponseEntity.ok( Encoder.encode( developers, GroupType.DEVELOPER ) );
    }
}
