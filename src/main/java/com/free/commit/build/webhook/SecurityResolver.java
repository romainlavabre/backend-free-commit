package com.free.commit.build.webhook;

import com.free.commit.api.request.Request;
import com.free.commit.entity.Project;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface SecurityResolver {

    boolean isBuildAllowed( Request request, Project project );
}
