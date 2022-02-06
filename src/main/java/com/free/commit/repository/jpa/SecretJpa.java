package com.free.commit.repository.jpa;

import com.free.commit.entity.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface SecretJpa extends JpaRepository< Secret, Long > {
}
