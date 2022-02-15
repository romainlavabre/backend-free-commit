package com.free.commit.build.webhook;

import com.free.commit.api.request.Request;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Developer;
import com.free.commit.entity.Project;
import com.free.commit.exception.HttpNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SecurityResolverImpl implements SecurityResolver {

    @Override
    public boolean isBuildAllowed( Request request, Project project ) {
        String  pusherLogin = getPusherLogin( request );
        boolean isGithub    = isGithub( request );
        boolean isAllowed   = false;

        for ( Developer developer : project.getDevelopers() ) {
            if ( isGithub && pusherLogin.equals( developer.getGithubUsername() ) ) {
                isAllowed = true;
                break;
            }
        }

        if ( isAllowed ) {
            isAllowed = isValidSignature( request, project );
        }

        return isAllowed;
    }


    protected boolean isValidSignature( Request request, Project project ) {
        if ( project.getSignatureKey() == null ) {
            return true;
        }

        if ( isGithub( request ) ) {
            String githubSignature = ( String ) request.getParameter( "X-Hub-Signature-256" );

            if ( githubSignature == null ) {
                return false;
            }

            try {
                MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
                byte[] encodedhash = digest.digest(
                        request.getBody().getBytes( StandardCharsets.UTF_8 ) );

                StringBuilder stringBuilder = new StringBuilder();
                for ( byte b : encodedhash ) {
                    stringBuilder.append( String.format( "%02x", b ) );
                }

                return ("sha256=" + stringBuilder.toString()).equals( githubSignature );
            } catch ( NoSuchAlgorithmException e ) {
                e.printStackTrace();
            }
        }

        return false;
    }


    protected String getPusherLogin( Request request ) {
        if ( isGithub( request ) ) {
            return request.getParameter( "sender_login" ).toString();
        }

        throw new HttpNotFoundException( Message.WEBHOOK_SENDER_NOT_FOUND );
    }


    protected boolean isGithub( Request request ) {
        return request.getHeader( "X-GitHub-Event" ) != null;
    }
}