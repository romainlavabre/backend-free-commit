package com.free.commit.repository;

import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Developer;
import com.free.commit.repository.jpa.DeveloperJpa;
import jakarta.persistence.EntityManager;
import org.romainlavabre.exception.HttpNotFoundException;
import org.romainlavabre.security.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DeveloperRepositoryImpl extends AbstractRepository< Developer > implements DeveloperRepository {

    protected final DeveloperJpa developerJpa;


    public DeveloperRepositoryImpl(
            EntityManager entityManager,
            DeveloperJpa developerJpa ) {
        super( entityManager, developerJpa );
        this.developerJpa = developerJpa;
    }


    @Override
    public Developer findOrFailByGithubUsername( String githubUsername ) {
        Optional< Developer > developerOptional = findByGithubUsername( githubUsername );

        if ( developerOptional.isPresent() ) {
            return developerOptional.get();
        }

        throw new HttpNotFoundException( Message.DEVELOPER_NOT_FOUND );
    }


    @Override
    public Optional< Developer > findByGithubUsername( String githubUsername ) {
        return developerJpa.findByGithubUsername( githubUsername );
    }


    @Override
    public Developer findOrFailByGitlabUsername( String gitlabUsername ) {
        Optional< Developer > developerOptional = findByGitlabUsername( gitlabUsername );

        if ( developerOptional.isPresent() ) {
            return developerOptional.get();
        }

        throw new HttpNotFoundException( Message.DEVELOPER_NOT_FOUND );
    }


    @Override
    public Optional< Developer > findByGitlabUsername( String gitlabUsername ) {
        return developerJpa.findByGitlabUsername( gitlabUsername );
    }


    @Override
    public Optional< Developer > findByUser( User user ) {
        return developerJpa.findByUser( user );
    }


    @Override
    public Developer findOrFailByUser( User user ) {
        Optional< Developer > developerOptional = findByUser( user );

        return developerOptional.orElseThrow( () -> new HttpNotFoundException( Message.DEVELOPER_NOT_FOUND ) );
    }


    @Override
    protected Class< Developer > getClassType() {
        return Developer.class;
    }
}
