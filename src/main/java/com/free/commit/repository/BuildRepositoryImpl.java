package com.free.commit.repository;

import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.repository.jpa.BuildJpa;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class BuildRepositoryImpl extends AbstractRepository< Build > implements BuildRepository {

    protected final BuildJpa buildJpa;


    public BuildRepositoryImpl(
            EntityManager entityManager,
            BuildJpa buildJpa ) {
        super( entityManager, buildJpa );
        this.buildJpa = buildJpa;
    }


    @Override
    public List< Build > findAllByProject( Project project ) {
        return buildJpa.findAllBuildByProject( project );
    }


    @Override
    protected Class< Build > getClassType() {
        return Build.class;
    }
}
