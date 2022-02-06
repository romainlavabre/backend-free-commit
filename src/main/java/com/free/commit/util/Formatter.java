package com.free.commit.util;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Formatter {
    public static String phone( String phone ) {
        if ( phone == null || phone.isBlank() ) {
            return null;
        }

        if ( phone.startsWith( "+" ) && phone.length() >= 12 && phone.length() <= 16 ) {
            return phone;
        }

        phone = phone.replaceAll( "[^0-9]", "" );

        if ( !phone.startsWith( "0" ) ) {
            phone = "+" + phone;
        }

        if ( phone.startsWith( "0" ) ) {
            return phone.replaceFirst( "^0", "+33" ).replaceAll( " ", "" );
        }

        return phone.replaceAll( " ", "" );
    }


    public static String name( String name ) {
        if ( name == null || name.isBlank() ) {
            return name;
        }

        return name.substring( 0, 1 ).toUpperCase() + name.substring( 1 );
    }


    public static String unity( double unity ) {
        String unityStr = String.valueOf( unity );
        unityStr = unityStr.replace( ".", "," );
        String decimals = unityStr.split( "," )[ 1 ];

        if ( decimals.length() > 2 ) {
            decimals = decimals.substring( 0, 1 );
        }

        if ( decimals.length() != 2 ) {
            decimals += "0";
        }

        return unityStr.split( "," )[ 0 ] + "," + decimals;
    }


    public static String unity( short unity ) {
        return unity( Double.valueOf( String.valueOf( unity ) ) );
    }
}
