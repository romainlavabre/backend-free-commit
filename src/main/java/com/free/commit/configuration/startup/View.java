package com.free.commit.configuration.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class View {

    protected final Logger        logger = LoggerFactory.getLogger( getClass() );
    protected final EntityManager entityManager;


    public View(
            EntityManager entityManager ) {
        this.entityManager = entityManager;
    }


    @Transactional
    @EventListener( ApplicationReadyEvent.class )
    public void createViews() throws IOException {
        Resource resource = new ClassPathResource( "view/project_pagination.sql" );

        entityManager.createNativeQuery( "DROP TABLE IF EXISTS project_pagination;" ).executeUpdate();
        entityManager.createNativeQuery( Files.readString( resource.getFile().toPath() ) ).executeUpdate();

        logger.info( "View project_pagination created" );
    }
}
