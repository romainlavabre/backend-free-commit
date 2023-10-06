package com.free.commit.controller.developer;

import com.free.commit.api.json.Encoder;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.Project;
import com.free.commit.repository.ProjectRepository;
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
@RestController( "DeveloperProjectController" )
@RequestMapping( path = "/developer" )
public class ProjectController {
    protected final ProjectRepository projectRepository;


    public ProjectController( ProjectRepository projectRepository ) {
        this.projectRepository = projectRepository;
    }


    @GetMapping( path = "/projects/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getProject( @PathVariable( "id" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        return ResponseEntity.ok( Encoder.encode( project, GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/projects" )
    public ResponseEntity< List< Map< String, Object > > > getAllProjects() {
        List< Project > projects = projectRepository.findAll();

        return ResponseEntity.ok( Encoder.encode( projects, GroupType.DEVELOPER ) );
    }
}
