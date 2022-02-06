package com.free.commit.api.json.put;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Put< T > {

    /**
     * @param entity Subject class
     * @return Value of putting field
     */
    Object build( T entity );
}
