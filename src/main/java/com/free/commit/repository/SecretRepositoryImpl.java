package com.free.commit.repository;

import com.free.commit.entity.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SecretRepositoryImpl extends AbstractRepository< Secret > implements SecretRepository {

    public SecretRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Secret, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Secret > getClassType() {
        return Secret.class;
    }
}
