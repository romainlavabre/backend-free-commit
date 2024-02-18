package com.free.commit.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Converter( autoApply = true )
public class JsonArrayColumnConverter implements AttributeConverter< JsonArrayColumn< Object >, String > {

    @Override
    public String convertToDatabaseColumn( JsonArrayColumn< Object > list ) {
        JSONArray jsonArray = new JSONArray( list );
        return jsonArray.toString();
    }


    @Override
    public JsonArrayColumn< Object > convertToEntityAttribute( String list ) {
        if ( list == null ) {
            return new JsonArrayColumn<>();
        }

        try {
            JSONArray                 jsonArray      = new JSONArray( list );
            JsonArrayColumn< Object > jsonListColumn = new JsonArrayColumn<>();
            jsonListColumn.addAll( jsonArray.toList() );

            return jsonListColumn;
        } catch ( JSONException e ) {
            e.printStackTrace();
            return new JsonArrayColumn<>();
        }
    }
}
