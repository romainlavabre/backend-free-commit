package com.free.commit.build.webhook;

import com.free.commit.api.request.Request;
import com.free.commit.build.Initiator;
import com.free.commit.configuration.response.Message;
import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Developer;
import com.free.commit.entity.Project;
import com.free.commit.exception.HttpNotFoundException;
import com.free.commit.repository.DeveloperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected final Logger              logger = LoggerFactory.getLogger( getClass() );
    protected final DeveloperRepository developerRepository;


    public SecurityResolverImpl( DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    @Override
    public Initiator isBuildAllowed( Request request, Project project ) {
        String    pusherLogin = getPusherLogin( request );
        String    ref         = getRef( request );
        boolean   isGithub    = isGithub( request );
        boolean   isGitlab    = isGitlab( request );
        boolean   isAllowed   = false;
        Developer developer   = null;

        logger.info( "Receive event of " + (isGithub ? "Github" : "Gitlab") + " for project " + project.getName() );

        if ( isGithub ) {
            developer = developerRepository.findOrFailByGithubUsername( pusherLogin );

            if ( developer.getUser().getRoles().contains( Role.ADMIN ) ) {
                isAllowed = true;
            } else {
                for ( Developer projectDeveloper : project.getDevelopers() ) {
                    if ( pusherLogin.equals( projectDeveloper.getGithubUsername() ) ) {
                        isAllowed = true;
                        break;
                    }
                }

                logger.info( "Developer " + pusherLogin + " is " + (isAllowed ? " allowed " : " not allowed ") + " to launch build" );
            }

            if ( isAllowed ) {
                ref       = ref.replace( "refs/heads/", "" );
                isAllowed = ref.equals( project.getBranch() );

                if ( isAllowed ) {
                    logger.warn( "Branch " + ref + " is concerned by this project" );
                } else {
                    logger.info( "Branch " + ref + " not concerned by this project" );
                }
            }
        }

        if ( isGitlab ) {
            developer = developerRepository.findOrFailByGitlabUsername( pusherLogin );

            if ( developer.getUser().getRoles().contains( Role.ADMIN ) ) {
                isAllowed = true;
            } else {
                for ( Developer projectDeveloper : project.getDevelopers() ) {
                    if ( pusherLogin.toLowerCase().trim().equals( projectDeveloper.getGithubUsername().toLowerCase().trim() ) ) {
                        isAllowed = true;
                        break;
                    }
                }

                logger.info( "Developer " + pusherLogin + " is " + (isAllowed ? " allowed " : " not allowed ") + " to launch build" );
            }

            if ( isAllowed ) {
                ref       = ref.replace( "refs/heads/", "" );
                isAllowed = ref.equals( project.getBranch() );

                logger.info( "Branch " + ref + " not concerned by project this project" );
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

                logger.info( "Computed signature: " + ("sha256=" + stringBuilder.toString()) );
                logger.info( "Computed signature: " + githubSignature );
                return ("sha256=" + stringBuilder.toString()).equals( githubSignature );
            } catch ( NoSuchAlgorithmException | InvalidKeyException e ) {
                e.printStackTrace();
            }
        }

        if ( isGitlab( request ) ) {
            String gitlabToken = request.getHeader( "X-Gitlab-Token" );

            return gitlabToken != null && gitlabToken.equals( project.getSignatureKey() );
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


    protected String getRef( Request request ) {
        return request.getParameter( "ref" ).toString();
    }


    protected boolean isGithub( Request request ) {
        return request.getHeader( "X-GitHub-Event" ) != null;
    }


    protected boolean isGitlab( Request request ) {
        return request.getHeader( "X-Gitlab-Event" ) != null;
    }
}
