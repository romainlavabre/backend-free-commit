package com.free.commit.build.webhook;

import com.free.commit.build.BuildManager;
import com.free.commit.build.Initiator;
import com.free.commit.entity.Project;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class WebhookHandlerImpl implements WebhookHandler {

    protected final SecurityResolver securityResolver;
    protected final BuildManager     buildManager;


    public WebhookHandlerImpl(
            SecurityResolver securityResolver,
            BuildManager buildManager ) {
        this.securityResolver = securityResolver;
        this.buildManager     = buildManager;
    }


    @Override
    public void handle( Request request, Project project ) {
        Initiator initiator = securityResolver.isBuildAllowed( request, project );

        if ( initiator.isAllowed() ) {
            buildManager.launch( project, initiator, request.getBody() );
        }
    }
}
