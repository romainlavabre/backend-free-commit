package com.free.commit.repository;

import com.free.commit.entity.Project;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ProjectRepositoryImpl extends AbstractRepository< Project > implements ProjectRepository {

    public ProjectRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Project, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Project > getClassType() {
        return Project.class;
    }
}
