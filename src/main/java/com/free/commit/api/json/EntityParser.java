package com.free.commit.api.json;

import com.free.commit.api.container.Container;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class EntityParser {

    private Class< ? > type;

    private Map< String, List< FieldParser > > fieldsParser;

    private Map< String, List< RowBuilder > > rowBuilders;


    public EntityParser( Class< ? > type, Container container ) {
        fieldsParser = new HashMap<>();
        rowBuilders  = new HashMap<>();
        this.type    = type;
        build( container );
    }


    public Map< String, Object > parse( Object entity, String group ) {
        Map< String, Object > result = new HashMap<>();

        if ( fieldsParser.containsKey( group ) ) {
            for ( FieldParser fieldParser : fieldsParser.get( group ) ) {
                FieldParser.ParsingResult parsingResult = fieldParser.parse( entity );

                if ( fieldParser.isAscent() && parsingResult.getValue() instanceof Map ) {
                    for ( Map.Entry< String, Object > entry : (( Map< String, Object > ) parsingResult.getValue()).entrySet() ) {
                        result.put( entry.getKey(), entry.getValue() );
                    }
                } else {
                    result.put( parsingResult.getKey(), parsingResult.getValue() );
                }
            }
        }

        if ( rowBuilders.containsKey( group ) ) {
            for ( RowBuilder rowBuilder : rowBuilders.get( group ) ) {
                FieldParser.ParsingResult parsingResult = rowBuilder.parse( entity );
                result.put( parsingResult.getKey(), parsingResult.getValue() );
            }
        }

        return result;
    }


    public Class< ? > getType() {
        return type;
    }


    private void build( Container container ) {
        for ( Field field : getFields() ) {
            for ( FieldParser fieldParser : FieldParser.build( field, container ) ) {
                if ( fieldsParser.containsKey( fieldParser.getGroup() ) ) {
                    fieldsParser.get( fieldParser.getGroup() ).add( fieldParser );
                    continue;
                }

                fieldsParser.put( fieldParser.getGroup(), new ArrayList<>() );
                fieldsParser.get( fieldParser.getGroup() ).add( fieldParser );
            }
        }

        for ( RowBuilder rowBuilder : RowBuilder.build( type, container ) ) {
            if ( rowBuilders.containsKey( rowBuilder.getGroup() ) ) {
                rowBuilders.get( rowBuilder.getGroup() ).add( rowBuilder );
                continue;
            }

            rowBuilders.put( rowBuilder.getGroup(), new ArrayList<>() );
            rowBuilders.get( rowBuilder.getGroup() ).add( rowBuilder );
        }
    }


    private List< Field > getFields() {
        List< Field > fields = new ArrayList<>();

        Arrays.asList( type.getDeclaredFields() ).forEach( fields::add );

        Class superClass = type.getSuperclass();

        while ( superClass != null ) {

            Arrays.asList( superClass.getDeclaredFields() ).forEach( field -> {
                if ( !fields.contains( field ) ) {
                    fields.add( field );
                }
            } );

            superClass = superClass.getSuperclass();
        }

        return fields;
    }
}
