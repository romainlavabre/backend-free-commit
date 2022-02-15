package com.free.commit.module.project;

import com.free.commit.api.crud.Update;
import com.free.commit.api.request.Request;
import com.free.commit.api.token.TokenGenerator;
import com.free.commit.entity.Project;
import com.free.commit.repository.ProjectRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectSignatureKey" )
public class UpdateSignatureKey implements Update< Project > {

    protected final ProjectRepository projectRepository;


    public UpdateSignatureKey( ProjectRepository projectRepository ) {
        this.projectRepository = projectRepository;
    }


    @Override
    public void update( Request request, Project project ) {
        project.setSignatureKey( TokenGenerator.generate( 32 ) );

        projectRepository.persist( project );
    }
}
