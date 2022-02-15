package com.free.commit.api.token;

import java.util.UUID;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TokenGenerator {


    public static String generate() {
        return generate( 8 );
    }


    public static String generate( int length ) {
        return UUID.randomUUID()
                   .toString()
                   .replace( "-", "" )
                   .substring( 0, length );
    }
}
