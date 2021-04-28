package com.replace.replace.api.event;

import com.replace.replace.api.event.annotation.Subscribers;
import com.replace.replace.api.event.annotation.UnitEvent;
import com.replace.replace.api.event.exception.InvalidEventCredentialsException;
import com.replace.replace.api.event.exception.NotRegisteredEventException;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventRegisterImpl implements EventRepository {

    protected EventConfiguration                  event;
    protected Map< String, Map< String, Class > > events;

    public EventRegisterImpl(
            EventConfiguration event )
            throws InvocationTargetException, IllegalAccessException {
        this.event = event;
        this.initEventRegistered();
    }

    @Override
    public void isValidCredentials( String event, Map< String, Object > params ) {

        if ( !this.events.containsKey( event ) ) {
            throw new NotRegisteredEventException();
        }


        for ( Map.Entry< String, Class > credentials : this.events.get( event ).entrySet() ) {

            if ( credentials.getValue() == null ) {
                continue;
            }

            
            if ( params.get( credentials.getKey() ).getClass() != credentials.getValue()
                    && !Proxy.isProxyClass( params.get( credentials.getKey() ).getClass() ) ) {
                throw new InvalidEventCredentialsException();
            }
        }
    }

    @Override
    public boolean hasDefaultSubscribers( String event ) {
        for ( Method method : EventConfiguration.class.getDeclaredMethods() ) {
            Subscribers subscribers = method.getAnnotation( Subscribers.class );

            if ( subscribers == null || !subscribers.event().equals( event ) ) {
                continue;
            }

            return true;
        }

        return false;
    }

    @Override
    public List< EventSubscriber > getDefaultSubscribers( String event ) {
        for ( Method method : EventConfiguration.class.getDeclaredMethods() ) {
            Subscribers subscribers = method.getAnnotation( Subscribers.class );

            if ( subscribers == null || !subscribers.event().equals( event ) ) {
                continue;
            }

            try {
                return ( List< EventSubscriber > ) method.invoke( this.event );
            } catch ( IllegalAccessException | InvocationTargetException e ) {
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

        for ( Method method : EventConfiguration.class.getDeclaredMethods() ) {
            UnitEvent unitEvent = method.getAnnotation( UnitEvent.class );

            if ( unitEvent == null ) {
                continue;
            }

            Map< String, Class > credentials = ( Map< String, Class > ) method.invoke( this.event );

            this.events.put( unitEvent.name(), credentials );
        }
    }
}
