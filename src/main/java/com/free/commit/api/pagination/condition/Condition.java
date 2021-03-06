package com.free.commit.api.pagination.condition;

import com.free.commit.api.pagination.Constraint;
import com.free.commit.api.pagination.exception.NotSupportedKey;
import com.free.commit.api.pagination.exception.NotSupportedOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Condition {

    protected static final Map< String, String > OPERATOR = Map.of(
            "eq", "=",
            "ne", "!=",
            "sup", ">",
            "inf", "<",
            "supeq", ">=",
            "infeq", "<=",
            "contains", "LIKE",
            "necontains", "NOT LIKE",
            "jsoncontains", "JSON_CONTAINS({column},{value})",
            "nejsoncontains", "!JSON_CONTAINS({column},{value})"
    );

    private final String key;

    private final String operator;

    private final List< String > values;

    private final Map< String, String > parameters;


    public Condition( final String key, final String operator ) {
        this.key        = key;
        this.operator   = operator;
        this.values     = new ArrayList<>();
        this.parameters = new HashMap<>();
    }


    public boolean isKey( final String key ) {
        return this.key.equals( key );
    }


    public boolean isOperator( final String operator ) {
        return this.operator.equals( operator );
    }


    public void addValue( final String value ) {
        this.values.add( value );
    }


    public Map< String, String > getParameters() {
        return this.parameters;
    }


    public String consume( final int startIncrement ) throws NotSupportedOperator, NotSupportedKey {

        Constraint.assertValidKey( this.key );

        final StringBuilder condition    = new StringBuilder( "( " );
        int                 keyIncrement = startIncrement * 1000;

        if ( this.values.size() > 1 ) {
            for ( int i = 0; i < this.values.size(); i++ ) {
                final String parameter = "key" + keyIncrement++;

                if ( !isFunction( operator ) ) {
                    final String operator = this.getSqlOperator( this.values.get( i ), parameter );
                    condition.append( this.key + " " + operator + " " + this.getParameter( this.values.get( i ), operator, parameter ) );
                } else {
                    condition.append( getFunction( values.get( i ), parameter ) );
                }


                if ( i < this.values.size() - 1 ) {
                    condition.append( " OR " );
                }
            }

            return condition.append( " )" ).toString();
        }

        return !isFunction( operator )
                ? this.key + " " + this.getSqlOperator( this.values.get( 0 ), "key" + keyIncrement ) + " " + this.getParameter( this.values.get( 0 ), this.operator, "key" + keyIncrement )
                : getFunction( values.get( 0 ), "key" + keyIncrement );
    }


    private String getSqlOperator( final String value, String parameter ) throws NotSupportedOperator {
        if ( !Condition.OPERATOR.containsKey( this.operator ) ) {
            throw new NotSupportedOperator( this.operator );
        }

        if ( value.equals( "null" ) ) {
            if ( this.operator.equals( "ne" ) ) {
                return "IS NOT NULL";
            } else {
                return "IS NULL";
            }
        }

        return Condition.OPERATOR.get( this.operator );
    }


    private String getParameter( final String value, final String operator, final String parameter ) {
        if ( operator.contains( "contains" ) ) {
            this.parameters.put( parameter, "%" + value + "%" );
            return ":" + parameter;
        } else if ( !value.toUpperCase().equals( "NULL" ) ) {
            this.parameters.put( parameter, value );
            return ":" + parameter;
        }

        return "";
    }


    private String getFunction( String value, String parameter ) throws NotSupportedOperator {
        if ( !Condition.OPERATOR.containsKey( this.operator ) ) {
            throw new NotSupportedOperator( this.operator );
        }

        this.parameters.put( parameter, value );
        return Condition.OPERATOR.get( this.operator ).replace( "{column}", this.key ).replace( "{value}", ":" + parameter );
    }


    private boolean isFunction( String operator ) {
        return operator.contains( "json" );
    }
}
