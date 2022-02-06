package com.free.commit.api.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@ResponseStatus( code = HttpStatus.INTERNAL_SERVER_ERROR )
public class EventSubscriberException extends RuntimeException {

    public EventSubscriberException() {
        super();
    }
}
