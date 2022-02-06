package com.free.commit.repository.jpa;

import com.free.commit.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface DeveloperJpa extends JpaRepository< Developer, Long > {
}
