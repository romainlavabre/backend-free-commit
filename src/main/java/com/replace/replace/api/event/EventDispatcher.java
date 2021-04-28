package com.replace.replace.api.event;

import java.util.Map;

/**
 * @author Romain Lavabre <romain.lavabre@fairfair.com>
 */
public interface EventDispatcher {

    /**
     * Subscribe to event
     *
     * @param event
     * @param service
     * @return
     */
    EventDispatcher follow( String event, EventSubscriber eventSubscriber );

    /**
     * Launch new event
     *
     * @param event
     * @param params
     * @return
     */
    EventDispatcher newEvent( String event, Map< String, Object > params );
}
