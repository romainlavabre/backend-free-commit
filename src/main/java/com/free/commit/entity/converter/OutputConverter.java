package com.free.commit.entity.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Base64;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class OutputConverter implements AttributeConverter< String, String > {


    @Override
    public String convertToDatabaseColumn( String s ) {
        return s != null
                ? Base64.getEncoder().encodeToString( s.getBytes() )
                : null;
    }


    @Override
    public String convertToEntityAttribute( String s ) {
        return s != null
                ? new String( Base64.getDecoder().decode( s.getBytes() ) )
                : null;
    }
}
