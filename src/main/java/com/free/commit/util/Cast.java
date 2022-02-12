package com.free.commit.util;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Cast {

    public static Long getLong( Object object ) {
        if ( object != null && !object.toString().isBlank() ) {
            return Long.valueOf( object.toString() );
        }

        return null;
    }


    public static Short getShort( Object object ) {
        if ( object != null && !object.toString().isBlank() ) {
            return Short.valueOf( object.toString() );
        }

        return null;
    }


    public static Byte getByte( Object object ) {
        if ( object != null && !object.toString().isBlank() ) {
            return Byte.valueOf( object.toString() );
        }

        return null;
    }


    public static Double getDouble( Object object ) {
        if ( object != null && !object.toString().isBlank() ) {
            return Double.valueOf( object.toString() );
        }

        return null;
    }


    public static Boolean getBoolean( Object object ) {
        if ( object != null && !object.toString().isBlank() ) {
            return Boolean.valueOf( object.toString() );
        }

        return null;
    }


    public static Integer getInteger( Object object ) {
        if ( object != null && !object.toString().isBlank() ) {
            return Integer.valueOf( object.toString() );
        }

        return null;
    }
}
