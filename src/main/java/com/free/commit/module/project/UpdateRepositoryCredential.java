package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.property.ProjectProperty;
import com.free.commit.repository.CredentialRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateProjectRepositoryCredential" )
public class UpdateRepositoryCredential implements Update< Project > {

    protected final ProjectRepository    projectRepository;
    protected final HistoryHandler       historyHandler;
    protected final CredentialRepository credentialRepository;


    public UpdateRepositoryCredential(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler,
            CredentialRepository credentialRepository ) {
        this.projectRepository    = projectRepository;
        this.historyHandler       = historyHandler;
        this.credentialRepository = credentialRepository;
    }


    @Override
    public void update( Request request, Project project ) {
        Long repositoryCredentialId = Cast.getLong( request.getParameter( ProjectParameter.REPOSITORY_CREDENTIAL ) );

        if ( repositoryCredentialId != null ) {
            project.setRepositoryCredential( credentialRepository.findOrFail( repositoryCredentialId ) );
        } else {
            project.setRepositoryCredential( null );
        }

        historyHandler.update( project, ProjectProperty.REPOSITORY_CREDENTIAL );

        projectRepository.persist( project );
    }
}
