package com.free.commit.build.webhook;

import com.free.commit.api.request.Request;
import com.free.commit.build.BuildManager;
import com.free.commit.entity.Project;
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
        if ( securityResolver.isBuildAllowed( request, project ) ) {
            buildManager.launch( project );
        }
    }
}
