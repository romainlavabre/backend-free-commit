package com.free.commit.configuration.startup;

import com.free.commit.entity.Executor;
import com.free.commit.repository.ExecutorRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DefaultExecutor {

    protected final Logger             logger = LoggerFactory.getLogger( getClass() );
    protected final ExecutorRepository executorRepository;
    protected final EntityManager      entityManager;


    public DefaultExecutor( ExecutorRepository executorRepository, EntityManager entityManager ) {
        this.executorRepository = executorRepository;
        this.entityManager      = entityManager;
    }


    @Transactional
    @EventListener( ApplicationReadyEvent.class )
    public void createDefaultAdmin() {
        if ( executorRepository.findAll().isEmpty() ) {
            Executor executor = new Executor();

            executor.setName( "default" );
            executor.setDriver( Executor.DRIVER_FREE_COMMIT );

            entityManager.persist( executor );
            entityManager.persist( executor );
            entityManager.flush();

            logger.info( "Executor " + executor.getName() + " was created" );
        }
    }
}
