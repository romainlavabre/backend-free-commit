package com.free.commit.build.webhook;

import com.free.commit.build.Initiator;
import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Developer;
import com.free.commit.entity.Project;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.repository.DeveloperRepositoryImpl;
import com.free.commit.repository.jpa.DeveloperJpa;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.romainlavabre.exception.HttpBadRequestException;
import org.romainlavabre.exception.HttpNotFoundException;
import org.romainlavabre.request.MockRequest;
import org.romainlavabre.request.Request;
import org.romainlavabre.security.User;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class SecurityResolverTest {

    private SecurityResolver getService( DeveloperRepository developerRepository ) {

        return new SecurityResolverImpl(
                new GithubSecurity( developerRepository ),
                new GitlabSecurity( developerRepository )
        );
    }


    @Test
    public void test_provider_not_supported() {
        SecurityResolver securityResolver = getService(
                new DeveloperRepositoryImpl( Mockito.mock( EntityManager.class ), Mockito.mock( DeveloperJpa.class ) )
        );

        Assertions.assertThrows(
                HttpBadRequestException.class,
                () -> securityResolver.isBuildAllowed( MockRequest.build( Map.of( "sender_login", "username" ) ), new Project() )
        );
    }


    @Test
    public void test_github_initiator_not_exists() {
        SecurityResolver securityResolver = getService(
                new DeveloperRepositoryImpl( Mockito.mock( EntityManager.class ), Mockito.mock( DeveloperJpa.class ) )
        );

        Assertions.assertThrows(
                HttpNotFoundException.class,
                () -> securityResolver.isBuildAllowed( MockRequest.build( Map.of( "sender_login", "username" ), Map.of(), Map.of( "X-GitHub-Event", "event" ) ), new Project() )
        );
    }


    @Test
    public void test_github_initiator_not_allowed_for_developer() {
        DeveloperRepository developerRepository = Mockito.mock( DeveloperRepository.class );

        Developer developer = new Developer();
        developer
                .setUser( new User() )
                .setGithubUsername( "username" );
        developer.getUser().addRole( Role.DEVELOPER );

        Mockito.when( developerRepository.findOrFailByGithubUsername( Mockito.anyString() ) )
                .thenReturn( developer );

        SecurityResolver securityResolver = getService( developerRepository );

        Request request = MockRequest.build( Map.of( "sender_login", "username", "ref", "master" ), Map.of(), Map.of( "X-GitHub-Event", "event" ) );
        Project project = new Project().setBranch( "master" );

        String signature = getSignature( project.getSignatureKey(), request );

        ( ( MockRequest ) request ).setHeader( "X-Hub-Signature-256", signature );

        Initiator initiator = securityResolver.isBuildAllowed( request, project );

        Assertions.assertFalse( initiator.isAllowed() );
    }


    @Test
    public void test_github_initiator_not_allowed_for_branch() {
        DeveloperRepository developerRepository = Mockito.mock( DeveloperRepository.class );

        Developer developer = new Developer();
        developer
                .setUser( new User() )
                .setGithubUsername( "username" );
        developer.getUser().addRole( Role.DEVELOPER );

        Mockito.when( developerRepository.findOrFailByGithubUsername( Mockito.anyString() ) )
                .thenReturn( developer );

        SecurityResolver securityResolver = getService( developerRepository );

        Request request = MockRequest.build( Map.of( "sender_login", "username", "ref", "master" ), Map.of(), Map.of( "X-GitHub-Event", "event" ) );
        Project project = new Project().addDeveloper( developer ).setBranch( "develop" );

        String signature = getSignature( project.getSignatureKey(), request );

        ( ( MockRequest ) request ).setHeader( "X-Hub-Signature-256", signature );

        Initiator initiator = securityResolver.isBuildAllowed( request, project );

        Assertions.assertFalse( initiator.isAllowed() );
    }


    @Test
    public void test_github_initiator_allowed() {
        DeveloperRepository developerRepository = Mockito.mock( DeveloperRepository.class );

        Developer developer = new Developer();
        developer
                .setUser( new User() )
                .setGithubUsername( "username" );
        developer.getUser().addRole( Role.DEVELOPER );

        Mockito.when( developerRepository.findOrFailByGithubUsername( Mockito.anyString() ) )
                .thenReturn( developer );

        SecurityResolver securityResolver = getService( developerRepository );

        Request request = MockRequest.build( Map.of( "sender_login", "username", "ref", "master" ), Map.of(), Map.of( "X-GitHub-Event", "event" ) );
        Project project = new Project().addDeveloper( developer ).setBranch( "master" );

        String signature = getSignature( project.getSignatureKey(), request );

        ( ( MockRequest ) request ).setHeader( "X-Hub-Signature-256", signature );

        Initiator initiator = securityResolver.isBuildAllowed( request, project );

        Assertions.assertTrue( initiator.isAllowed() );
    }


    private String getSignature( String key, Request request ) {
        try {
            Mac           mac           = Mac.getInstance( "HmacSHA256" );
            SecretKeySpec secretKeySpec = new SecretKeySpec( key.getBytes(), "HmacSHA256" );
            mac.init( secretKeySpec );
            byte[] encodedHash = mac.doFinal( request.getBody().getBytes() );

            StringBuilder stringBuilder = new StringBuilder();
            for ( byte b : encodedHash ) {
                stringBuilder.append( String.format( "%02x", b ) );
            }

            return "sha256=" + stringBuilder;
        } catch ( NoSuchAlgorithmException | InvalidKeyException e ) {
            e.printStackTrace();
        }

        return null;
    }
}
