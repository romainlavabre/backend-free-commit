package com.free.commit.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.json.JSONObject;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Converter( autoApply = true )
public class JsonObjectColumnConverter implements AttributeConverter< JsonObjectColumn< String, Object >, String > {

    @Override
    public String convertToDatabaseColumn( JsonObjectColumn< String, Object > map ) {
        if ( map == null ) {
            return null;
        }

        return JSONObject.valueToString( map );
    }


    @Override
    public JsonObjectColumn< String, Object > convertToEntityAttribute( String map ) {
        if ( map == null ) {
            return null;
        }

        JSONObject jsonObject = new JSONObject( map );

        JsonObjectColumn< String, Object > result = new JsonObjectColumn<>();
        result.putAll( jsonObject.toMap() );

        return result;
    }
}
