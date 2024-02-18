package com.free.commit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication( scanBasePackages = { "com.free.commit", "org.romainlavabre" } )
@EntityScan( { "com.free.commit", "org.romainlavabre" } )
@EnableJpaRepositories( { "com.free.commit", "org.romainlavabre" } )
public class FreeCommitApplication {

    public static void main( final String[] args ) {
        SpringApplication.run( FreeCommitApplication.class, args );
    }
}
