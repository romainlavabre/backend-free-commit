package com.free.commit.api.history;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequestScope
public class HistoryHandlerImpl implements HistoryHandler {
    private final Map< Object, History > store;
    private final EntityManager          entityManager;
    private final List<HistorySubscriber> historySubscribers;


    public HistoryHandlerImpl(
            final EntityManager entityManager,
            List<HistorySubscriber> historySubscribers) {
        this.entityManager = entityManager;
        this.store         = new HashMap<>();
        this.historySubscribers = historySubscribers;
    }


    @Override
    public void create( final Object object ) {
        assert object != null : "variable object should not be null";

        final History history = new History();
        history.setAuthorId( 0 );
        history.setAuthorType( "Unknow" );
        history.setSubjectType( object.getClass().getName() );
        history.setLogType( History.TYPE_CREATE );
        history.setIpAddress( this.getRemoteAddr() );

        this.store.put( object, history );

        for ( HistorySubscriber historySubscriber : historySubscribers ){
            historySubscriber.create( object, history );
        }
    }


    @Override
    public void update( final Object object, final String property ) {

        assert object != null : "variable object should not be null";

        final History history = new History();
        history.setAuthorId( 0 );
        history.setAuthorType( "Unknow" );
        history.setSubjectType( object.getClass().getName() );
        history.setSubjectProperty( property );
        history.setNewValue( this.getFieldValue( object, property ) );
        history.setLogType( History.TYPE_UPDATE );
        history.setIpAddress( this.getRemoteAddr() );

        this.store.put( object, history );

        for ( HistorySubscriber historySubscriber : historySubscribers ){
            historySubscriber.update( object, property, history );
        }
    }


    @Override
    public void delete( final Object object ) {
        assert object != null : "variable object should not be null";

        final History history = new History();
        history.setAuthorId( 0 );
        history.setAuthorType( "Unknow" );
        history.setSubjectId( Integer.valueOf( this.getFieldValue( object, "id" ) ) );
        history.setSubjectType( object.getClass().getName() );
        history.setLogType( History.TYPE_DELETE );
        history.setIpAddress( this.getRemoteAddr() );

        this.entityManager.persist( history );

        for ( HistorySubscriber historySubscriber : historySubscribers ){
            historySubscriber.delete( object, history );
        }
    }


    /**
     * Return new value of subject property target
     *
     * @param object
     * @param property
     * @return
     */
    private String getFieldValue( final Object object, final String property ) {
        Field reflectionProperty = null;

        Class objectClass = object.getClass();

        while ( true ) {
            try {
                reflectionProperty = objectClass.getDeclaredField( property );
                reflectionProperty.setAccessible( true );
                return String.valueOf( reflectionProperty.get( object ) );
            } catch ( final NoSuchFieldException | IllegalAccessException e ) {
                if ( objectClass.getSuperclass() != null ) {
                    objectClass = objectClass.getSuperclass();
                    continue;
                }

                return null;
            }
        }
    }


    private String getRemoteAddr() {
        return (( ServletRequestAttributes ) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRemoteAddr();
    }


    @Override
    public void receiveEvent( final String event, final Map< String, Object > params ) throws RuntimeException {
        for ( final Map.Entry< Object, History > entry : this.store.entrySet() ) {
            final History history = entry.getValue();

            history.setSubjectId( Integer.parseInt( this.getFieldValue( entry.getKey(), "id" ) ) );


            this.entityManager.persist( history );
        }
    }


    @Override
    public int getPriority() {
        return 0;
    }
}
