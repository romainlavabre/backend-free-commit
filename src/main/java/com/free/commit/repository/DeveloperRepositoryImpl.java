package com.free.commit.repository;

import com.free.commit.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DeveloperRepositoryImpl extends AbstractRepository< Developer > implements DeveloperRepository {

    public DeveloperRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Developer, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Developer > getClassType() {
        return Developer.class;
    }
}
