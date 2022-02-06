package com.free.commit.api.upload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@ResponseStatus( code = HttpStatus.UNPROCESSABLE_ENTITY )
public class DuplicationException extends RuntimeException {

    public DuplicationException( final String message ) {
        super( message );
    }
}
