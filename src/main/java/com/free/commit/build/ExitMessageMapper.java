package com.free.commit.build;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class ExitMessageMapper {
    public static final Map< Integer, String > MAPPER;

    static {
        MAPPER = new HashMap<>();

        MAPPER.put( -1, "Unknown error" );
        MAPPER.put( 1000, "Spec file not found" );
        MAPPER.put( 1001, "Spec file not readable" );
        MAPPER.put( 2000, "Custom deployment error" );
        MAPPER.put( 143, "Deployment aborted by user" );
    }
}
