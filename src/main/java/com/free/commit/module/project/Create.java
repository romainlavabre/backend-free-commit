package com.free.commit.module.project;

import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.repository.*;
import com.free.commit.util.Cast;
import org.romainlavabre.history.HistoryHandler;
import org.romainlavabre.request.Request;
import org.romainlavabre.tokengen.TokenGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createProject" )
public class Create implements org.romainlavabre.crud.Create< Project > {

    protected final ProjectRepository    projectRepository;
    protected final HistoryHandler       historyHandler;
    protected final DeveloperRepository  developerRepository;
    protected final SecretRepository     secretRepository;
    protected final CredentialRepository credentialRepository;
    protected final ExecutorRepository   executorRepository;


    public Create(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler,
            DeveloperRepository developerRepository,
            SecretRepository secretRepository,
            CredentialRepository credentialRepository,
            ExecutorRepository executorRepository ) {
        this.projectRepository    = projectRepository;
        this.historyHandler       = historyHandler;
        this.developerRepository  = developerRepository;
        this.secretRepository     = secretRepository;
        this.credentialRepository = credentialRepository;
        this.executorRepository   = executorRepository;
    }


    @Override
    public void create( Request request, Project project ) {
        String         name                     = ( String ) request.getParameter( ProjectParameter.NAME );
        String         description              = ( String ) request.getParameter( ProjectParameter.DESCRIPTION );
        String         repository               = ( String ) request.getParameter( ProjectParameter.REPOSITORY );
        String         branch                   = ( String ) request.getParameter( ProjectParameter.BRANCH );
        String         specFilePath             = ( String ) request.getParameter( ProjectParameter.SPEC_FILE_PATH );
        Integer        keepNumberBuild          = Cast.getInteger( request.getParameter( ProjectParameter.KEEP_NUMBER_BUILD ) );
        Boolean        allowConcurrentExecution = Cast.getBoolean( request.getParameter( ProjectParameter.ALLOW_CONCURRENT_EXECUTION ) );
        List< Object > developersId             = request.getParameters( ProjectParameter.DEVELOPERS );
        List< Object > secretsId                = request.getParameters( ProjectParameter.SECRETS );
        Long           repositoryCredentialId   = Cast.getLong( request.getParameter( ProjectParameter.REPOSITORY_CREDENTIAL ) );
        Long           executorId               = request.getParameter( ProjectParameter.EXECUTOR, Long.class );

        project.setName( name )
                .setDescription( description )
                .setRepository( repository )
                .setBranch( branch )
                .setSpecFilePath( specFilePath )
                .setKeepNumberBuild( keepNumberBuild )
                .setAllowConcurrentExecution( allowConcurrentExecution )
                .setSignatureKey( TokenGenerator.generate( 32 ) )
                .setExecutor( executorRepository.findOrFail( executorId ) );

        if ( repositoryCredentialId != null ) {
            project.setRepositoryCredential( credentialRepository.findOrFail( repositoryCredentialId ) );
        }

        if ( developersId != null ) {
            for ( Object developerId : developersId ) {
                project.addDeveloper( developerRepository.findOrFail( Cast.getLong( developerId ) ) );
            }
        }

        if ( secretsId != null ) {
            for ( Object secretId : secretsId ) {
                project.addSecret( secretRepository.findOrFail( Cast.getLong( secretId ) ) );
            }
        }

        historyHandler.create( project );

        projectRepository.persist( project );
    }
}
