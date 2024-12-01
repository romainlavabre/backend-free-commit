package com.free.commit.entity.converter;

import com.free.commit.entity.encrypt.EncryptField;
import jakarta.persistence.AttributeConverter;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class JsonObjectColumnEncryptConverter implements AttributeConverter< JsonObjectColumn< String, Object >, String > {
    protected final EncryptField encryptField;


    public JsonObjectColumnEncryptConverter( EncryptField encryptField ) {
        this.encryptField = encryptField;
    }


    @Override
    public String convertToDatabaseColumn( JsonObjectColumn< String, Object > map ) {
        if ( map == null ) {
            return null;
        }

        return encryptField.convertToDatabaseColumn( JSONObject.valueToString( map ) );
    }


    @Override
    public JsonObjectColumn< String, Object > convertToEntityAttribute( String map ) {
        if ( map == null ) {
            return null;
        }

        map = encryptField.convertToEntityAttribute( map );

        JSONObject jsonObject = new JSONObject( map );

        JsonObjectColumn< String, Object > result = new JsonObjectColumn<>();
        result.putAll( jsonObject.toMap() );

        return result;
    }
}
