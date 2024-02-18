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

@Service
public class GitlabSecurity {


    protected final Logger              logger = LoggerFactory.getLogger( getClass() );
    protected final DeveloperRepository developerRepository;


    public GitlabSecurity( DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    public Initiator isBuildAllowed( Request request, Project project, String eventInitiator ) {
        logger.debug( "Receive event of Gitlab for project " + project.getName() );
        String    branch    = request.getParameter( "ref", String.class );
        Developer developer = developerRepository.findOrFailByGitlabUsername( eventInitiator );

        boolean isAllowed = false;

        if ( developer.getUser().getRoles().contains( Role.ADMIN ) ) {
            isAllowed = true;
        } else {
            for ( Developer projectDeveloper : project.getDevelopers() ) {
                if ( eventInitiator.toLowerCase().trim().equals( projectDeveloper.getGithubUsername().toLowerCase().trim() ) ) {
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

            isAllowed = branch.equals( project.getBranch() );

            logger.debug( "Branch " + branch + " not concerned by project this project" );
        }

        if ( isAllowed ) {
            isAllowed = isValidSignature( request, project );

            if ( isAllowed ) {
                logger.debug( "Signature allowed" );
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

        String gitlabToken = request.getHeader( "X-Gitlab-Token" );

        return gitlabToken != null && gitlabToken.equals( project.getSignatureKey() );
    }
}
