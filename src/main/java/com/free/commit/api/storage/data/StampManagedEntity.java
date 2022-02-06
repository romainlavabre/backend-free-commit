package com.free.commit.api.storage.data;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class StampManagedEntity {

    private final List< Object > persistedEntities;
    private final List< Object > removedEntities;


    public StampManagedEntity() {
        this.persistedEntities = new ArrayList<>();
        this.removedEntities   = new ArrayList<>();
    }


    public void buffer( final Object entity ) {
        if ( !this.persistedEntities.contains( entity ) ) {
            this.persistedEntities.add( entity );
        }
    }


    public void remove( final Object entity ) {
        this.persistedEntities.remove( entity );

        this.removedEntities.add( entity );
    }


    public void collectBuffer( final EntityManager entityManager ) {

        final Iterator< Object > iteratorPersistedEntities = this.persistedEntities.iterator();

        while ( iteratorPersistedEntities.hasNext() ) {
            final Object entity = iteratorPersistedEntities.next();

            entityManager.persist( entity );

            iteratorPersistedEntities.remove();
        }

        final Iterator< Object > iteratorRemovedEntities = this.removedEntities.iterator();

        while ( iteratorRemovedEntities.hasNext() ) {
            final Object entity = iteratorRemovedEntities.next();

            entityManager.remove( entity );

            iteratorRemovedEntities.remove();
        }
    }
}
