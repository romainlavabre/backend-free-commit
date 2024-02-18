package com.free.commit.build.webhook;

import com.free.commit.build.Initiator;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Project;
import org.romainlavabre.exception.HttpBadRequestException;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SecurityResolverImpl implements SecurityResolver {

    protected final GithubSecurity githubSecurity;
    protected final GitlabSecurity gitlabSecurity;


    public SecurityResolverImpl( GithubSecurity githubSecurity, GitlabSecurity gitlabSecurity ) {
        this.githubSecurity = githubSecurity;
        this.gitlabSecurity = gitlabSecurity;
    }


    @Override
    public Initiator isBuildAllowed( Request request, Project project ) {

        if ( isGithub( request ) ) {
            String eventInitiator = request.getParameter( "sender_login", String.class );

            return githubSecurity.isBuildAllowed( request, project, eventInitiator );
        }

        if ( isGitlab( request ) ) {
            String eventInitiator = request.getParameter( "user_name", String.class );
            
            return gitlabSecurity.isBuildAllowed( request, project, eventInitiator );
        }

        throw new HttpBadRequestException( Message.WEBHOOK_PROVIDER_NOT_SUPPORTED );
    }


    protected boolean isGithub( Request request ) {
        return request.getHeader( "X-GitHub-Event" ) != null;
    }


    protected boolean isGitlab( Request request ) {
        return request.getHeader( "X-Gitlab-Event" ) != null;
    }
}
