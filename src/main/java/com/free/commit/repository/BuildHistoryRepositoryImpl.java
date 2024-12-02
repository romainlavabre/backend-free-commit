package com.free.commit.repository;

import com.free.commit.entity.BuildHistory;
import com.free.commit.repository.jpa.BuildHistoryJpa;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 * @author romain.lavabre@proton.me
 */
@Service
public class BuildHistoryRepositoryImpl extends AbstractRepository< BuildHistory > implements BuildHistoryRepository {
    protected final BuildHistoryJpa buildHistoryJpa;


    public BuildHistoryRepositoryImpl( EntityManager entityManager, BuildHistoryJpa buildHistoryJpa ) {
        super( entityManager, buildHistoryJpa );
        this.buildHistoryJpa = buildHistoryJpa;
    }


    @Override
    protected Class< BuildHistory > getClassType() {
        return BuildHistory.class;
    }
}
