package com.free.commit.api.rest;

import kong.unirest.Header;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class ResponseImpl implements Response {

    protected HttpResponse< JsonNode > response;


    @Override
    public int status() {
        if ( this.response != null ) {
            return this.response.getStatus();
        }

        return 0;
    }


    @Override
    public boolean isSuccess() {
        if ( this.response != null ) {
            return this.response.getStatus() >= 200
                    && this.response.getStatus() < 300;
        }

        return false;
    }


    @Override
    public boolean hasHeader( final String header ) {
        if ( this.response != null ) {
            return this.response.getHeaders().containsKey( header );
        }

        return false;
    }


    @Override
    public String getHeader( final String header ) {
        if ( this.response != null ) {
            return this.response.getHeaders().get( header ).get( 0 );
        }

        return null;
    }


    @Override
    public Map< String, String > getHeaders() {

        if ( this.response == null ) {
            return new HashMap<>();
        }

        final Map< String, String > headers = new HashMap<>();

        for ( final Header header : this.response.getHeaders().all() ) {
            headers.put( header.getName(), header.getValue() );
        }

        return headers;
    }


    @Override
    public Map< String, Object > getBodyAsMap() {
        if ( this.response != null ) {
            return this.response.getBody().getObject().toMap();
        }

        return new HashMap<>();
    }


    @Override
    public List< Object > getBodyAsList() {
        if ( this.response != null ) {
            return this.response.getBody().getArray().toList();
        }

        return new ArrayList<>();
    }


    protected Response supply( final HttpResponse< JsonNode > response ) {
        this.response = response;

        return this;
    }


    protected Response supply() {
        this.response = null;

        return this;
    }
}
