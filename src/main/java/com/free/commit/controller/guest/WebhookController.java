package com.free.commit.controller.guest;

import com.free.commit.api.request.Request;
import com.free.commit.api.storage.data.DataStorageHandler;
import com.free.commit.build.webhook.WebhookHandler;
import com.free.commit.entity.Project;
import com.free.commit.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "GuestWebhookController" )
@RequestMapping( path = "/guest/webhooks" )
public class WebhookController {

    protected final ProjectRepository  projectRepository;
    protected final WebhookHandler     webhookHandler;
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;


    public WebhookController(
            ProjectRepository projectRepository,
            WebhookHandler webhookHandler,
            DataStorageHandler dataStorageHandler,
            Request request ) {
        this.projectRepository  = projectRepository;
        this.webhookHandler     = webhookHandler;
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
    }


    @Transactional
    @PostMapping( path = "/build/{projectId:[0-9]+}" )
    public ResponseEntity< Void > build( @PathVariable( "projectId" ) long id ) {
        Project project = projectRepository.findOrFail( id );

        webhookHandler.handle( request, project );

        return ResponseEntity.status( HttpStatus.CREATED ).build();
    }
}
