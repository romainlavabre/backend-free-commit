package com.free.commit.module.developer;

import com.free.commit.api.crud.Update;
import com.free.commit.api.history.HistoryHandler;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.ProjectRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperRole" )
public class UpdateRole implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;
    protected final ProjectRepository   projectRepository;


    public UpdateRole(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler,
            ProjectRepository projectRepository ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
        this.projectRepository   = projectRepository;
    }


    @Override
    public void update( Request request, Developer developer ) {
        String role = request.getParameter( DeveloperParameter.ROLE, String.class );

        developer.getUser().getRoles().clear();
        developer.getUser().addRole( role );

        historyHandler.update( developer, DeveloperProperty.ROLES );

        developerRepository.persist( developer );
    }
}
