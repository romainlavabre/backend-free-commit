package com.free.commit.repository;

import com.free.commit.api.security.User;
import com.free.commit.entity.Developer;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DeveloperRepository extends DefaultRepository< Developer > {
    Developer findOrFailByGithubUsername( String githubUsername );


    Optional< Developer > findByGithubUsername( String githubUsername );


    Developer findOrFailByGitlabUsername( String gitlabUsername );


    Optional< Developer > findByGitlabUsername( String gitlabUsername );


    Optional< Developer > findByUser( User user );


    Developer findOrFailByUser( User user );
}
