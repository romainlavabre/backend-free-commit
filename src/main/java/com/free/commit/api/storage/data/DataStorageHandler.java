package com.free.commit.api.storage.data;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DataStorageHandler {

    /**
     * Lock line
     *
     * @param entity
     * @param type
     */
    void lock( Object entity, int type );


    /**
     * Follow this entity
     *
     * @param entity Target unit of persistence
     */
    void persist( Object entity );


    /**
     * @param entity Target unit of persistence
     */
    void remove( Object entity );


    /**
     * Commit in database
     */
    void save();
}
