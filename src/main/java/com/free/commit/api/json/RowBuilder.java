package com.free.commit.api.json;

import com.free.commit.api.container.Container;
import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.JsonPut;
import com.free.commit.api.json.annotation.Row;
import com.free.commit.api.json.put.Put;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class RowBuilder {

    private String group;

    private Put put;

    private String key;


    public FieldParser.ParsingResult parse( Object entity ) {
        return FieldParser.ParsingResult.of(
                key,
                put.build( entity )
        );
    }


    public static List< RowBuilder > build( Class< ? > entity, Container container ) {
        List< RowBuilder > result  = new ArrayList<>();
        JsonPut            jsonPut = entity.getAnnotation( JsonPut.class );

        if ( jsonPut == null ) {
            return result;
        }

        for ( Group group : jsonPut.groups() ) {
            for ( Row row : group.row() ) {
                RowBuilder rowBuilder = new RowBuilder();
                rowBuilder.group = group.name();
                rowBuilder.key   = row.key();
                rowBuilder.put   = ( Put ) container.getInstance( row.handler() );

                result.add( rowBuilder );
            }
        }

        return result;
    }


    public String getGroup() {
        return group;
    }
}
