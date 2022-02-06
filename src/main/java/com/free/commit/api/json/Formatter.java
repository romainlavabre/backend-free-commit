package com.free.commit.api.json;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Formatter {

    public static String toSnakeCase( String value ) {
        char[] characters = value.toCharArray();
        int    iterator   = characters.length;

        StringBuilder stringBuilder = new StringBuilder();

        for ( int i = 0; i < iterator; i++ ) {
            if ( Character.isLetter( characters[ i ] ) ) {
                if ( Character.isUpperCase( characters[ i ] ) ) {
                    if ( i > 0 ) {
                        if ( characters[ i - 1 ] != '_' ) {
                            stringBuilder.append( "_" + Character.toLowerCase( characters[ i ] ) );
                        } else {
                            stringBuilder.append( Character.toLowerCase( characters[ i ] ) );
                        }
                    } else {
                        stringBuilder.append( Character.toLowerCase( characters[ i ] ) );
                    }
                } else {
                    stringBuilder.append( Character.toLowerCase( characters[ i ] ) );
                }

                continue;
            }

            if ( Character.isDigit( characters[ i ] ) ) {
                if ( i > 0 ) {
                    if ( characters[ i - 1 ] != '_' ) {
                        stringBuilder.append( "_" + characters[ i ] );
                    } else {
                        stringBuilder.append( characters[ i ] );
                    }
                } else {
                    stringBuilder.append( characters[ i ] );
                }

                continue;
            }

            stringBuilder.append( characters[ i ] );
        }

        return stringBuilder.toString();
    }
}
