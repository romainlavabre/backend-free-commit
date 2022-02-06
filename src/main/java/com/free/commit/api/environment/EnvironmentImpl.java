package com.free.commit.api.environment;

import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EnvironmentImpl implements Environment {

    protected org.springframework.core.env.Environment environment;


    public EnvironmentImpl( final org.springframework.core.env.Environment environment ) {
        this.environment = environment;
    }


    @Override
    public String getEnv( final String env ) {
        assert env != null && !env.isBlank() : "variable env should not be null or blank";

        return this.environment.getProperty( env );
    }
}
