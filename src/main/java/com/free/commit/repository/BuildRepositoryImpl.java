package com.free.commit.repository;

import com.free.commit.entity.Build;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class BuildRepositoryImpl extends AbstractRepository< Build > implements BuildRepository {

    public BuildRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Build, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Build > getClassType() {
        return Build.class;
    }
}
