package com.free.commit.configuration.history;

import org.romainlavabre.history.HistoryDataProvider;
import org.romainlavabre.request.Request;
import org.romainlavabre.security.Security;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class HistoryConfigurerImpl implements HistoryDataProvider {

    protected final Security security;
    protected final Request  request;


    public HistoryConfigurerImpl( Security security, Request request ) {
        this.security = security;
        this.request  = request;
    }


    @Override
    public Optional< Integer > getAuthorId() {
        return Optional.empty();
    }


    @Override
    public Optional< String > getAuthorName() {
        if ( !security.hasUserConnected() ) {
            return Optional.of( "Guest" );
        }

        if ( security.getUsername() == null || security.getUsername().isBlank() ) {
            return Optional.of( "SYSTEM" );
        }

        return Optional.ofNullable( security.getUsername() );
    }


    @Override
    public Optional< String > getAuthorIp() {
        return Optional.ofNullable( request.getHeader( "Cf-Connecting-Ip" ) );
    }
}
