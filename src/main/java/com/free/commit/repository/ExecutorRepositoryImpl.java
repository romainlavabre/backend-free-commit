package com.free.commit.repository;

import com.free.commit.entity.Executor;
import com.free.commit.repository.jpa.ExecutorJpa;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class ExecutorRepositoryImpl extends AbstractRepository< Executor > implements ExecutorRepository {
    public ExecutorRepositoryImpl( EntityManager entityManager, ExecutorJpa executorJpa ) {
        super( entityManager, executorJpa );
    }


    @Override
    protected Class< Executor > getClassType() {
        return Executor.class;
    }
}
