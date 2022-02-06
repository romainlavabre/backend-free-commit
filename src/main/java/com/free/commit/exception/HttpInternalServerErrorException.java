package com.free.commit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@ResponseStatus( code = HttpStatus.INTERNAL_SERVER_ERROR )
public class HttpInternalServerErrorException extends RuntimeException {

    public HttpInternalServerErrorException( String message ) {
        super( message );
    }
}
