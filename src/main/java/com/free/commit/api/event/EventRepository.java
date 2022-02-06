package com.free.commit.api.event;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface EventRepository {

    /**
     * Control that provided parameters is valid
     *
     * @param event
     * @param params
     */
    void isValidCredentials( String event, Map< String, Object > params );


    /**
     * @param event
     * @return TRUE if target event has default subscribers
     */
    boolean hasDefaultSubscribers( String event );


    /**
     * Return all default subscribers where potentially ignored (Not Application scoped)
     *
     * @param event
     * @return
     */
    List< EventSubscriber > getDefaultSubscribers( String event );
}
