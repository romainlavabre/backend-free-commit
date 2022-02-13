package com.free.commit.repository;

import com.free.commit.entity.Secret;
import com.free.commit.repository.jpa.SecretJpa;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SecretRepositoryImpl extends AbstractRepository< Secret > implements SecretRepository {

    protected final SecretJpa secretJpa;


    public SecretRepositoryImpl(
            EntityManager entityManager,
            SecretJpa secretJpa ) {
        super( entityManager, secretJpa );
        this.secretJpa = secretJpa;
    }


    @Override
    public List< Secret > findAllWithGlobalScope() {
        return secretJpa.findAllByProjectIsNull();
    }


    @Override
    protected Class< Secret > getClassType() {
        return Secret.class;
    }
}
