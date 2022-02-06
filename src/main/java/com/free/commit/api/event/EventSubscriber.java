package com.free.commit.api.event;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 * {@see https://github.com/romainlavabre/spring-starter-event.git}
 */
public interface EventSubscriber {

    /**
     * Will receive event target where is launched
     *
     * @param event
     * @param params
     */
    void receiveEvent( String event, Map< String, Object > params )
            throws RuntimeException;


    /**
     * 0 implies that it is not a priority, so it will be called at the end,
     * 1 indicates a very high priority and more is a lower priority
     *
     * @return priority
     */
    int getPriority();
}
