package com.free.commit.module.project;

import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.repository.SecretRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createProject" )
public class Create implements com.free.commit.api.crud.Create< Project > {

    protected final ProjectRepository   projectRepository;
    protected final HistoryHandler      historyHandler;
    protected final DeveloperRepository developerRepository;
    protected final SecretRepository    secretRepository;


    public Create(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler,
            DeveloperRepository developerRepository,
            SecretRepository secretRepository ) {
        this.projectRepository   = projectRepository;
        this.historyHandler      = historyHandler;
        this.developerRepository = developerRepository;
        this.secretRepository    = secretRepository;
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

        project.setName( name )
               .setDescription( description )
               .setRepository( repository )
               .setBranch( branch )
               .setSpecFilePath( specFilePath )
               .setKeepNumberBuild( keepNumberBuild )
               .setAllowConcurrentExecution( allowConcurrentExecution );

        for ( Object developerId : developersId ) {
            project.addDeveloper( developerRepository.findOrFail( Cast.getLong( developerId ) ) );
        }

        for ( Object secretId : secretsId ) {
            project.addSecret( secretRepository.findOrFail( Cast.getLong( secretId ) ) );
        }

        historyHandler.create( project );

        projectRepository.persist( project );
    }
}
