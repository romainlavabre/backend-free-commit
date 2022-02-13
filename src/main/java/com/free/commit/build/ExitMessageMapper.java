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

        MAPPER.put( 1000, "Spec file not found" );
    }
}
