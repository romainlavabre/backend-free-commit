package com.free.commit.configuration.security;

import com.free.commit.configuration.environment.Variable;
import com.free.commit.util.Cast;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.security.config.SecurityConfigurer;
import org.springframework.stereotype.Service;

@Service
public class ConfigureSecurity {

    protected final Environment environment;


    public ConfigureSecurity( Environment environment ) {
        this.environment = environment;
        configure();
    }


    private void configure() {
        SecurityConfigurer
                .init()
                .addPublicEndpoint( "/guest/**" )
                .addPublicEndpoint( "/auth/**" )

                .addSecuredEndpoint( "/admin/**", Role.ADMIN )
                .addSecuredEndpoint( "/developer/**", Role.DEVELOPER )

                .setJwtSecret( environment.getEnv( Variable.JWT_SECRET ) )
                .setJwtLifeTime( Cast.getInteger( environment.getEnv( Variable.JWT_LIFE_TIME ) ) )
                .build();

    }
}
