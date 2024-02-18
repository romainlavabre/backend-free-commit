package com.free.commit.repository;

import com.free.commit.entity.Credential;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class CredentialRepositoryImpl extends AbstractRepository< Credential > implements CredentialRepository {

    public CredentialRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Credential, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Credential > getClassType() {
        return Credential.class;
    }
}
