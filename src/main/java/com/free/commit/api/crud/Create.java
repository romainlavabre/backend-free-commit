package com.free.commit.api.crud;

import com.free.commit.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Create< E > {
    void create( Request request, E entity );
}
