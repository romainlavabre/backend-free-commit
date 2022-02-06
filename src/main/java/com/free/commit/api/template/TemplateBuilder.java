package com.free.commit.api.template;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface TemplateBuilder {
    String build( String name );


    String build( String name, Map< String, Object > parameters );
}
