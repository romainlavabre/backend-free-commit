package com.free.commit.api.event;

import com.free.commit.api.event.annotation.Subscribers;
import com.free.commit.api.event.annotation.UnitEvent;
import com.free.commit.api.event.exception.InvalidEventCredentialsException;
import com.free.commit.api.event.exception.NotRegisteredEventException;
import com.free.commit.configuration.event.EventConfig;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventRegisterImpl implements EventRepository {

    protected EventConfig                         event;
    protected Map< String, Map< String, Class > > events;


    public EventRegisterImpl(
            final EventConfig event )
            throws InvocationTargetException, IllegalAccessException {
        this.event = event;
        this.initEventRegistered();
    }


    @Override
    public void isValidCredentials( final String event, final Map< String, Object > params ) {

        if ( !this.events.containsKey( event ) ) {
            throw new NotRegisteredEventException();
        }


        for ( final Map.Entry< String, Class > credentials : this.events.get( event ).entrySet() ) {

            if ( credentials.getValue() == null ) {
                continue;
            }


            if ( params.get( credentials.getKey() ).getClass() != credentials.getValue()
                    && !credentials.getValue().isAssignableFrom( params.get( credentials.getKey() ).getClass() ) ) {
                throw new InvalidEventCredentialsException();
            }
        }
    }


    @Override
    public boolean hasDefaultSubscribers( final String event ) {
        for ( final Method method : EventConfig.class.getDeclaredMethods() ) {
            final Subscribers subscribers = method.getAnnotation( Subscribers.class );

            if ( subscribers == null || !subscribers.event().equals( event ) ) {
                continue;
            }

            return true;
        }

        return false;
    }


    @Override
    public List< EventSubscriber > getDefaultSubscribers( final String event ) {
        for ( final Method method : EventConfig.class.getDeclaredMethods() ) {
            final Subscribers subscribers = method.getAnnotation( Subscribers.class );

            if ( subscribers == null || !subscribers.event().equals( event ) ) {
                continue;
            }

            try {
                return ( List< EventSubscriber > ) method.invoke( this.event );
            } catch ( final IllegalAccessException | InvocationTargetException e ) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * Init all event registered by developer
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void initEventRegistered() throws InvocationTargetException, IllegalAccessException {
        this.events = new HashMap<>();

        for ( final Method method : EventConfig.class.getDeclaredMethods() ) {
            final UnitEvent unitEvent = method.getAnnotation( UnitEvent.class );

            if ( unitEvent == null ) {
                continue;
            }

            final Map< String, Class > credentials = ( Map< String, Class > ) method.invoke( this.event );

            this.events.put( unitEvent.name(), credentials );
        }
    }
}
