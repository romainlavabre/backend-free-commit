package com.free.commit.build.webhook;

import com.free.commit.build.Initiator;
import com.free.commit.entity.Project;
import org.romainlavabre.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface SecurityResolver {

    Initiator isBuildAllowed( Request request, Project project );
}
