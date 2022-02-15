package com.free.commit.repository.jpa;

import com.free.commit.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface DeveloperJpa extends JpaRepository< Developer, Long > {
    Optional< Developer > findByGithubUsername( String githubUsername );


    Optional< Developer > findByGitlabUsername( String gitlabUsername );
}
