package com.free.commit.repository;

import com.free.commit.entity.Secret;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public interface SecretRepository extends DefaultRepository< Secret > {

    List< Secret > findAllWithGlobalScope();
}
