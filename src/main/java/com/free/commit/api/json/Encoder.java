package com.free.commit.api.json;

import com.free.commit.api.container.Container;
import com.free.commit.configuration.json.GroupType;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Encoder {

    private static Encoder              instance;
    private final  List< EntityParser > entitiesParser;
    private final  Container            container;


    public Encoder( Container container ) {
        entitiesParser = new ArrayList<>();
        instance       = this;
        this.container = container;
    }


    public static < T > Map< String, Object > encode( final T entity ) {

        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static Map< String, Object > encode( final Map< String, Object > entity ) {
        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static < T > List< Map< String, Object > > encode( final List< T > entity ) {
        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static < T > List< Map< String, Object > > encode( final T[] entity ) {
        return Encoder.encode( entity, GroupType.DEFAULT );
    }


    public static < T > Map< String, Object > encode( final T entity, final String group ) {
        return Encoder.core( entity, group );
    }


    public static < T > Map< String, Object > encode( final Map< String, T > entity, final String group ) {
        final Map< String, Object > map = new HashMap<>();

        for ( final Map.Entry< String, T > entry : entity.entrySet() ) {
            map.put( entry.getKey(), Encoder.core( entry.getValue(), group ) );
        }

        return map;
    }


    public static < T > List< Map< String, Object > > encode( final List< T > entity, final String group ) {

        final List< Map< String, Object > > list = new ArrayList<>();

        for ( final T unit : entity ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }


    public static < T > List< Map< String, Object > > encode( final T[] entities, final String group ) {
        final List< Map< String, Object > > list = new ArrayList<>();

        for ( final T unit : entities ) {
            list.add( Encoder.core( unit, group ) );
        }

        return list;
    }


    protected static Map< String, Object > core( final Object entity, final String targetGroup ) {
        Class< ? > search;

        if ( entity instanceof HibernateProxy ) {
            search = entity.getClass().getSuperclass();
        } else {
            search = entity.getClass();
        }

        for ( EntityParser entityParser : instance.entitiesParser ) {
            if ( entityParser.getType().equals( search ) ) {
                return entityParser.parse( entity, targetGroup );
            }
        }

        EntityParser entityParser = new EntityParser( search, instance.container );

        instance.entitiesParser.add( entityParser );

        return entityParser.parse( entity, targetGroup );
    }
}
