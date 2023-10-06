package com.free.commit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@ResponseStatus( code = HttpStatus.BAD_REQUEST )
public class HttpBadRequestException extends RuntimeException {
    public HttpBadRequestException( String message ) {
        super( message );
    }
}
