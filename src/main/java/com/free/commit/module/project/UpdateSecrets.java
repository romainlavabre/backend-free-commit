package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.entity.Secret;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectSecrets" )
public class UpdateSecrets implements Update< Project > {

    protected final ProjectRepository   projectRepository;
    protected final HistoryHandler      historyHandler;
    protected final SecretRepository    secretRepository;


    public UpdateSecrets( ProjectRepository projectRepository, HistoryHandler historyHandler, SecretRepository secretRepository ) {
        this.projectRepository = projectRepository;
        this.historyHandler    = historyHandler;
        this.secretRepository  = secretRepository;
    }


    @Override
    public void update( Request request, Project project ) {
        List< Object > secretsId = request.getParameters( ProjectParameter.SECRETS );

        for ( Secret secret : project.getSecrets() ) {
            secret.removeProject( project );
        }

        project.getSecrets().clear();

        for ( Object secretId : secretsId ) {
            project.addSecret( secretRepository.findOrFail( Cast.getLong( secretId ) ) );
        }

        historyHandler.update( project, ProjectProperty.SECRETS );

        projectRepository.persist( project );
    }
}
