package com.free.commit.api.event;

import java.util.Map;

/**
 * @author Romain Lavabre <romain.lavabre@fairfair.com>
 * {@see https://github.com/romainlavabre/spring-starter-event.git}
 */
public interface EventDispatcher {

    /**
     * Subscribe to event
     *
     * @param event
     * @param eventSubscriber
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
