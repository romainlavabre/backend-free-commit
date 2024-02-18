package com.free.commit.configuration.security;

import org.romainlavabre.security.config.SecurityConfigurer;
import org.springframework.stereotype.Service;

@Service
public class ConfigureSecurity {

    public ConfigureSecurity() {
        configure();
    }


    private void configure() {
        SecurityConfigurer
                .init()
                .addPublicEndpoint( "/guest/**" )
                .addPublicEndpoint( "/auth/**" )
                .addPublicEndpoint( "/application/**" )
                .addPublicEndpoint( "/actuator/health" )

                .addSecuredEndpoint( "/admin/**", Role.ADMIN )
                .addSecuredEndpoint( "/developer/**", Role.DEVELOPER )
                .build();

    }
}
