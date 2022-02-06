package com.free.commit.api.json;

import com.free.commit.api.container.Container;
import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.api.json.overwritter.DefaultOverwrite;
import com.free.commit.api.json.overwritter.Overwrite;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class FieldParser {

    private String group;

    private Overwrite overwrite;

    private String key;

    private boolean relation;

    private boolean onlyId;

    private boolean ascent;

    private Field field;

    private boolean isCollectionOrArray;

    private Field relationFieldId;


    public ParsingResult parse( Object entity ) {
        Object value;

        try {
            value = field.get( entity );
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
            return ParsingResult.of( key );
        }

        Object newValue;

        if ( value != null ) {
            if ( relation ) {
                if ( isCollectionOrArray ) {
                    newValue = new ArrayList<>();

                    for ( Object subValue : ( Collection ) value ) {
                        if ( subValue == null ) {
                            continue;
                        }
                        
                        (( List ) newValue).add( onlyId ? getIdOfRelation( subValue ) : Encoder.encode( subValue, group ) );
                    }
                } else {
                    newValue = onlyId ? getIdOfRelation( value ) : Encoder.encode( value, group );
                }
            } else {
                newValue = value;
            }
        } else {
            newValue = null;
        }


        if ( overwrite != null ) {
            return ParsingResult.of(
                    key,
                    overwrite.overwrite( entity )
            );
        }

        return ParsingResult.of(
                key,
                newValue
        );
    }


    private Long getIdOfRelation( Object relation ) {
        try {
            return ( Long ) relationFieldId.get( relation );
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
            return null;
        }
    }


    public static List< FieldParser > build( Field field, Container container ) {
        List< FieldParser > result = new ArrayList<>();
        Json                json   = field.getAnnotation( Json.class );

        if ( json == null ) {
            return result;
        }

        field.setAccessible( true );

        for ( Group group : json.groups() ) {
            FieldParser fieldParser = new FieldParser();
            fieldParser.group               = group.name();
            fieldParser.ascent              = group.ascent();
            fieldParser.onlyId              = group.onlyId();
            fieldParser.overwrite           = group.overwrite() != DefaultOverwrite.class ? container.getInstance( group.overwrite() ) : null;
            fieldParser.field               = field;
            fieldParser.isCollectionOrArray = TypeResolver.isArrayOrCollection( field );

            if ( group.forceEncoding()
                    || (field.getType().getAnnotation( Entity.class ) != null)
                    || (Collection.class.isAssignableFrom( field.getType() ) && ((( Class< ? > ) (( ParameterizedType ) field.getGenericType()).getActualTypeArguments()[ 0 ]).isAnnotationPresent( Entity.class ))) ) {
                fieldParser.relation = true;
            }

            if ( fieldParser.relation && group.onlyId() ) {
                try {
                    if ( fieldParser.isCollectionOrArray ) {
                        fieldParser.relationFieldId = (( Class< ? > ) (( ParameterizedType ) field.getGenericType()).getActualTypeArguments()[ 0 ]).getDeclaredField( "id" );
                    } else {
                        fieldParser.relationFieldId = field.getType().getDeclaredField( "id" );
                    }
                } catch ( NoSuchFieldException e ) {
                    e.printStackTrace();
                }

                fieldParser.relationFieldId.setAccessible( true );
            }

            fieldParser.key = !group.key().isBlank() ? group.key() : (fieldParser.relation && fieldParser.onlyId ? Formatter.toSnakeCase( field.getName() ) + "_id" : Formatter.toSnakeCase( field.getName() ));

            result.add( fieldParser );
        }

        return result;
    }


    public String getGroup() {
        return group;
    }


    public boolean isAscent() {
        return ascent;
    }


    public static class ParsingResult {
        private String key;

        private Object value;


        public static ParsingResult of( String key ) {
            ParsingResult parsingResult = new ParsingResult();
            parsingResult.key   = key;
            parsingResult.value = null;
            return parsingResult;
        }


        public static ParsingResult of( String key, Object value ) {
            ParsingResult parsingResult = new ParsingResult();
            parsingResult.key   = key;
            parsingResult.value = value;
            return parsingResult;
        }


        public String getKey() {
            return key;
        }


        public Object getValue() {
            return value;
        }
    }
}
