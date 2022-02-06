package com.free.commit.api.crud;

import com.free.commit.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Update< E > {
    void update( Request request, E entity );
}
