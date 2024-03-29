package com.free.commit.build.webhook;

import com.free.commit.entity.Project;
import org.romainlavabre.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface WebhookHandler {
    void handle( Request request, Project project );
}
