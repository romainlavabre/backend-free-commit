package com.free.commit.entity;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.converter.JsonObjectColumn;
import com.free.commit.entity.converter.JsonObjectColumnEncryptConverter;
import jakarta.persistence.*;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.exception.HttpUnprocessableEntityException;

@Entity
public class Executor {
    public static final String DRIVER_FREE_COMMIT = "localDriver";
    public static final String DRIVER_OPEN_STACK  = "openStackDriver";

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String name;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String driver;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    @Convert( converter = JsonObjectColumnEncryptConverter.class )
    @Column( columnDefinition = "TEXT" )
    private JsonObjectColumn< String, Object > variables;


    public Executor() {
        variables = new JsonObjectColumn();
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Executor setName( String name ) {
        if ( name == null || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.EXECUTOR_NAME_REQUIRED );
        }

        this.name = name;

        return this;
    }


    public String getDriver() {
        return driver;
    }


    public Executor setDriver( String driver ) {
        if ( driver == null || driver.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.EXECUTOR_DRIVER_REQUIRED );
        }

        if ( !driver.equals( DRIVER_FREE_COMMIT )
                && !driver.equals( DRIVER_OPEN_STACK ) ) {
            throw new HttpUnprocessableEntityException( Message.EXECUTOR_DRIVER_INVALID );
        }

        this.driver = driver;

        return this;
    }


    public JsonObjectColumn< String, Object > getVariables() {
        return variables;
    }


    public Executor addVariable( String key, Object value ) {
        variables.put( key, value );

        return this;
    }
}
