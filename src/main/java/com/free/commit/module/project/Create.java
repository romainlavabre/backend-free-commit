package com.free.commit.module.project;

import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;
import com.free.commit.parameter.ProjectParameter;
import com.free.commit.repository.ProjectRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "createProject" )
public class Create implements com.free.commit.api.crud.Create< Project > {

    protected final ProjectRepository projectRepository;
    protected final HistoryHandler    historyHandler;


    public Create(
            ProjectRepository projectRepository,
            HistoryHandler historyHandler ) {
        this.projectRepository = projectRepository;
        this.historyHandler    = historyHandler;
    }


    @Override
    public void create( Request request, Project project ) {
        String  name            = ( String ) request.getParameter( ProjectParameter.NAME );
        String  description     = ( String ) request.getParameter( ProjectParameter.DESCRIPTION );
        String  repository      = ( String ) request.getParameter( ProjectParameter.REPOSITORY );
        String  branch          = ( String ) request.getParameter( ProjectParameter.BRANCH );
        String  specFilePath    = ( String ) request.getParameter( ProjectParameter.SPEC_FILE_PATH );
        Integer keepNumberBuild = Cast.getInteger( request.getParameter( ProjectParameter.KEEP_NUMBER_BUILD ) );

        project.setName( name )
               .setDescription( description )
               .setRepository( repository )
               .setBranch( branch )
               .setSpecFilePath( specFilePath )
               .setKeepNumberBuild( keepNumberBuild );

        historyHandler.create( project );

        projectRepository.persist( project );
    }
}
