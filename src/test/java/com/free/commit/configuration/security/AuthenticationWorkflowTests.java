package com.free.commit.configuration.security;

import ch.vorburger.exec.ManagedProcessException;
import com.free.commit.database.DatabaseProvider;
import com.free.commit.util.Cast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@ActiveProfiles( { "test" } )
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_CLASS )
public class AuthenticationWorkflowTests {
    @Autowired
    private WebTestClient webTestClient;


    @BeforeAll
    public static void initDB() throws ManagedProcessException {
        DatabaseProvider.initDB();
    }


    @Test
    public void entryPoint() {
        Map< String, Object > authResult = auth();

        Assertions.assertNotNull( authResult.get( "access_token" ) );
        Assertions.assertEquals( 3600, Cast.getInteger( authResult.get( "expires_in" ) ) );
        Assertions.assertEquals( "Bearer", authResult.get( "token_type" ) );
    }


    private Map< String, Object > auth() {
        FluxExchangeResult< Map > result =
                webTestClient
                        .post()
                        .uri( "/auth/token" )
                        .bodyValue( Map.of(
                                "auth", Map.of(
                                        "username", "root",
                                        "password", "root"
                                )
                        ) )
                        .exchange()
                        .expectStatus()
                        .is2xxSuccessful()
                        .returnResult( Map.class );

        return result.getResponseBody().blockFirst();
    }

}
