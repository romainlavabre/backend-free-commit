package com.free.commit.api.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class MockRequest implements Request {


    protected final Map< String, Object >       parameters;
    protected final Map< String, UploadedFile > files;


    public MockRequest() {
        this.parameters = new HashMap<>();
        this.files      = new HashMap<>();
    }


    @Override
    public boolean containsParameter( final String name ) {
        return this.parameters.containsKey( name );
    }


    @Override
    public Object getParameter( final String name ) {
        return this.parameters.get( name );
    }


    @Override
    public void setParameter( final String name, final Object value ) {
        this.parameters.put( name, value );
    }


    @Override
    public List< Object > getParameters( final String name ) {
        return ( List< Object > ) this.parameters.get( name );
    }


    @Override
    public Map< String, Object > getAllParameters() {
        return this.parameters;
    }


    @Override
    public Map< String, Object > getAllParameters( final String prefix ) {
        final Map< String, Object > parameters = new HashMap<>();

        for ( final Map.Entry< String, Object > entry : parameters.entrySet() ) {
            if ( entry.getKey().startsWith( prefix ) ) {
                parameters.put( entry.getKey().replace( prefix, "" ), entry.getValue() );
            }
        }

        return parameters;
    }


    @Override
    public String getRawQueryString() {
        return null;
    }


    @Override
    public String getQueryString( final String name ) {
        return null;
    }


    @Override
    public void setQueryString( final String name, final String value ) {

    }


    @Override
    public UploadedFile getFile( final String name ) {
        return this.files.get( name );
    }


    @Override
    public List< UploadedFile > getFiles( final String name ) {
        final List< UploadedFile > list = new ArrayList<>();

        this.files.forEach( ( key, value ) -> {
            list.add( value );
        } );

        return list;
    }


    @Override
    public void setUploadedFile( final String name, final UploadedFile uploadedFile ) {
        this.files.put( name, uploadedFile );
    }


    @Override
    public String getClientIp() {
        return null;
    }


    @Override
    public String getHeader( final String name ) {
        return null;
    }


    @Override
    public String getContentType() {
        return "application/json";
    }


    @Override
    public Integer getPort() {
        return 8085;
    }


    @Override
    public String getHost() {
        return "localhost";
    }


    @Override
    public String getScheme() {
        return "http";
    }


    @Override
    public String getUri() {
        return "/api";
    }


    @Override
    public String getBaseUrl() {
        return "http://localhost:8085";
    }


    @Override
    public String getMethod() {
        return null;
    }


    @Override
    public String getBody() {
        return "";
    }


    public static Request build( final Map< String, Object > parameters, final Map< String, UploadedFile > files ) {

        final Request request = new MockRequest();

        parameters.forEach( request::setParameter );
        files.forEach( request::setUploadedFile );

        return request;
    }


    public static Request build( final Map< String, Object > parameters ) {

        final Request request = new MockRequest();

        parameters.forEach( request::setParameter );

        return request;
    }


    public static Request build() {
        return new MockRequest();
    }
}
