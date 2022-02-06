package com.free.commit.api.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventDispatcherImpl implements EventDispatcher {

    protected final Map< String, List< EventSubscriber > > subscribers;
    protected final EventRepository                        eventRepository;
    protected       Logger                                 logger = LoggerFactory.getLogger( this.getClass() );


    public EventDispatcherImpl( final EventRepository eventRepository ) {
        this.subscribers     = new HashMap<>();
        this.eventRepository = eventRepository;
    }


    @Override
    public EventDispatcher follow( final String event, final EventSubscriber eventSubscriber ) {

        this.logger.info( eventSubscriber.getClass().getName() + " as subscribed to event \"" + event + "\"" );

        if ( !this.isInitializedEvent( event ) ) {
            this.initializeEventSubscribers( event );
        }

        if ( !this.subscribers.get( event ).contains( eventSubscriber ) ) {
            this.subscribers.get( event ).add( eventSubscriber );
        }

        return this;
    }


    @Override
    public EventDispatcher newEvent( final String event, final Map< String, Object > params ) {

        this.eventRepository.isValidCredentials( event, params );


        final List< EventSubscriber > subscribers = new ArrayList<>();

        if ( this.eventRepository.hasDefaultSubscribers( event ) ) {
            for ( final EventSubscriber eventSubscriber : this.eventRepository.getDefaultSubscribers( event ) ) {
                subscribers.add( eventSubscriber );
            }
        }

        if ( this.isInitializedEvent( event ) ) {
            for ( final EventSubscriber eventSubscriber : this.subscribers.get( event ) ) {
                if ( subscribers.contains( eventSubscriber ) ) {
                    continue;
                }

                subscribers.add( eventSubscriber );
            }
        }
        
        for ( final EventSubscriber eventSubscriber : this.sortSubscribers( subscribers ) ) {
            eventSubscriber.receiveEvent( event, params );
        }

        return this;
    }


    /**
     * Control that event is already initialized in subscribers repository
     */
    protected boolean isInitializedEvent( final String event ) {
        return this.subscribers.containsKey( event );
    }


    /**
     * Initialize event in subscribers repository
     */
    protected void initializeEventSubscribers( final String event ) {

        this.subscribers.put( event, new ArrayList<>() );
    }


    protected List< EventSubscriber > sortSubscribers( final List< EventSubscriber > eventSubscribers ) {
        final List< EventSubscriber > result = new ArrayList<>();

        final Map< Integer, List< EventSubscriber > > temporarySort = new HashMap<>();

        int maxPriority = 0;

        for ( final EventSubscriber eventSubscriber : eventSubscribers ) {
            if ( eventSubscriber.getPriority() > maxPriority ) {
                maxPriority = eventSubscriber.getPriority();
            }

            if ( temporarySort.containsKey( eventSubscriber.getPriority() ) ) {
                temporarySort.get( eventSubscriber.getPriority() ).add( eventSubscriber );
                continue;
            }

            final List< EventSubscriber > listByPriority = new ArrayList<>();
            listByPriority.add( eventSubscriber );

            temporarySort.put( eventSubscriber.getPriority(), listByPriority );
        }

        for ( int i = 1; i <= maxPriority; i++ ) {
            if ( temporarySort.containsKey( i ) ) {
                result.addAll( temporarySort.get( i ) );
            }
        }

        if ( temporarySort.containsKey( 0 ) ) {
            result.addAll( temporarySort.get( 0 ) );
        }

        return result;
    }
}
