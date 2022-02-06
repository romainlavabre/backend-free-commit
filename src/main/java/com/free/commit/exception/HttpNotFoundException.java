package com.free.commit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.NOT_FOUND )
public class HttpNotFoundException extends RuntimeException {

    public HttpNotFoundException( String message ) {
        super( message );
    }
}
