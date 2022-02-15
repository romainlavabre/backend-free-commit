package com.free.commit.build.webhook;

import com.free.commit.api.request.Request;
import com.free.commit.configuration.response.Message;
import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Developer;
import com.free.commit.entity.Project;
import com.free.commit.exception.HttpNotFoundException;
import com.free.commit.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SecurityResolverImpl implements SecurityResolver {

    protected final DeveloperRepository developerRepository;


    public SecurityResolverImpl( DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    @Override
    public boolean isBuildAllowed( Request request, Project project ) {
        String  pusherLogin = getPusherLogin( request );
        boolean isGithub    = isGithub( request );
        boolean isGitlab    = isGitlab( request );
        boolean isAllowed   = false;

        if ( isGithub ) {
            Developer developer = developerRepository.findOrFailByGithubUsername( pusherLogin );

            if ( developer.getUser().getRoles().contains( Role.ADMIN ) ) {
                isAllowed = true;
            } else {
                for ( Developer projectDeveloper : project.getDevelopers() ) {
                    if ( pusherLogin.equals( projectDeveloper.getGithubUsername() ) ) {
                        isAllowed = true;
                        break;
                    }
                }
            }
        }

        if ( isGitlab ) {
            Developer developer = developerRepository.findOrFailByGitlabUsername( pusherLogin );

            if ( developer.getUser().getRoles().contains( Role.ADMIN ) ) {
                isAllowed = true;
            } else {
                for ( Developer projectDeveloper : project.getDevelopers() ) {
                    if ( pusherLogin.equals( projectDeveloper.getGithubUsername() ) ) {
                        isAllowed = true;
                        break;
                    }
                }
            }

            System.out.println( isAllowed );
        }

        if ( isAllowed ) {
            isAllowed = isValidSignature( request, project );
            System.out.println( isAllowed );
        }

        return isAllowed;
    }


    protected boolean isValidSignature( Request request, Project project ) {
        if ( project.getSignatureKey() == null ) {
            return true;
        }

        if ( isGithub( request ) ) {
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

                return ("sha256=" + stringBuilder.toString()).equals( githubSignature );
            } catch ( NoSuchAlgorithmException | InvalidKeyException e ) {
                e.printStackTrace();
            }
        }

        if ( isGitlab( request ) ) {
            System.out.println( request.getHeader( "X-Gitlab-Token" ) );
        }

        return false;
    }


    protected String getPusherLogin( Request request ) {
        if ( isGithub( request ) ) {
            return request.getParameter( "sender_login" ).toString();
        }

        if ( isGitlab( request ) ) {
            return request.getParameter( "user_name" ).toString();
        }

        throw new HttpNotFoundException( Message.WEBHOOK_SENDER_NOT_FOUND );
    }


    protected boolean isGithub( Request request ) {
        return request.getHeader( "X-GitHub-Event" ) != null;
    }


    protected boolean isGitlab( Request request ) {
        return request.getHeader( "X-Gitlab-Event" ) != null;
    }
}
