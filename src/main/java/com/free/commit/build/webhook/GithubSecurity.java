package com.free.commit.build.webhook;


import com.free.commit.build.Initiator;
import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Developer;
import com.free.commit.entity.Project;
import com.free.commit.repository.DeveloperRepository;
import org.romainlavabre.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class GithubSecurity {
    protected final Logger              logger = LoggerFactory.getLogger( getClass() );
    protected final DeveloperRepository developerRepository;


    public GithubSecurity( DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    public Initiator isBuildAllowed( Request request, Project project, String eventInitiator ) {
        String    branch    = request.getParameter( "ref", String.class );
        Developer developer = developerRepository.findOrFailByGithubUsername( eventInitiator );
        boolean   isAllowed = false;

        if ( developer.getUser().getRoles().contains( Role.ADMIN ) ) {
            isAllowed = true;
        } else {
            for ( Developer projectDeveloper : project.getDevelopers() ) {
                if ( eventInitiator.equals( projectDeveloper.getGithubUsername() ) ) {
                    isAllowed = true;
                    break;
                }
            }

            logger.debug( "Developer " + eventInitiator + " is " + ( isAllowed ? " allowed " : " not allowed " ) + " to launch build" );
        }

        if ( isAllowed ) {
            if ( branch != null ) {
                branch = branch.replace( "refs/heads/", "" );
            }

            isAllowed = project.getBranch().equals( "*" ) || branch.equals( project.getBranch() );

            if ( isAllowed ) {
                logger.debug( "Branch " + branch + " is concerned by this project" );
            } else {
                logger.debug( "Branch " + branch + " not concerned by this project" );
            }
        }

        if ( isAllowed ) {
            isAllowed = isValidSignature( request, project );

            if ( isAllowed ) {
                logger.info( "Signature allowed" );
            } else {
                logger.warn( "Invalid signature" );
            }
        }

        return new Initiator(
                developer.getEmail(),
                isAllowed
        );
    }


    protected boolean isValidSignature( Request request, Project project ) {
        if ( project.getSignatureKey() == null ) {
            return true;
        }

        String githubSignature = request.getHeader( "X-Hub-Signature-256" );

        if ( githubSignature == null ) {
            return false;
        }

        try {
            Mac           mac           = Mac.getInstance( "HmacSHA256" );
            SecretKeySpec secretKeySpec = new SecretKeySpec( project.getSignatureKey().getBytes(), "HmacSHA256" );
            mac.init( secretKeySpec );
            byte[] encodedHash = mac.doFinal( request.getBody().getBytes() );

            StringBuilder stringBuilder = new StringBuilder();
            for ( byte b : encodedHash ) {
                stringBuilder.append( String.format( "%02x", b ) );
            }

            logger.debug( "Computed signature: " + ( "sha256=" + stringBuilder ) );
            logger.debug( "Github signature: " + githubSignature );
            return ( "sha256=" + stringBuilder ).equals( githubSignature );
        } catch ( NoSuchAlgorithmException | InvalidKeyException e ) {
            e.printStackTrace();
        }


        return false;
    }
}
