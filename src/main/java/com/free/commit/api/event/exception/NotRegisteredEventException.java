package com.free.commit.api.event.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@ResponseStatus( code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An event was called but is never registered" )
public class NotRegisteredEventException extends RuntimeException {

    public NotRegisteredEventException() {
        super();
    }
}
