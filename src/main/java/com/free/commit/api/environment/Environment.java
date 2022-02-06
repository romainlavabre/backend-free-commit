package com.free.commit.api.environment;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 * {@see https://github.com/romainlavabre/spring-starter-environment.git}
 */
public interface Environment {

    /**
     * @param env Environment name
     * @return Value of environment if found, null otherwise.
     */
    String getEnv( String env );
}
