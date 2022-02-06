package com.free.commit.api.upload;

import com.free.commit.api.request.UploadedFile;
import com.free.commit.api.upload.exception.UploadException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manage upload system
 *
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Scope( value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES )
public class UploadHandlerImpl implements UploadHandler {

    protected Configuration               configuration;
    protected Map< UploadedFile, String > triggers = new HashMap<>();


    public UploadHandlerImpl( final Configuration configuration ) {
        this.configuration = configuration;
    }


    @Override
    public boolean upload( final UploadedFile uploadedFile, final String config ) {
        this.assertValidSize( uploadedFile.getSize(), config );
        this.assertValidContentType( uploadedFile.getContentType(), config );
        this.configuration.getMoveRule( config ).setDestination( uploadedFile );
        this.configuration.getDuplicationRule( config ).exec( uploadedFile );

        if ( this.configuration.isTransactionSynchronized( config ) ) {
            this.triggers.put( uploadedFile, config );
        }

        this.configuration.getMoveRule( config ).move( uploadedFile );


        return false;
    }


    /**
     * Control that valid file has valid size
     *
     * @param size   Size of uploaded file
     * @param config Configuration
     * @throws UploadException If is invalid size
     */
    protected void assertValidSize( final int size, final String config )
            throws UploadException {

        if ( !this.configuration.hasMaxSize( config ) ) {
            return;
        }

        if ( this.configuration.getSize( config ) < size ) {
            throw new UploadException( "To long file provided (max: " + size + ")" );
        }
    }


    /**
     * Control that file has valid content type
     *
     * @param contentType Content type of file
     * @param config      Configuration
     * @throws UploadException If is invalid content type
     */
    protected void assertValidContentType( final String contentType, final String config )
            throws UploadException {

        if ( this.configuration.isAcceptAllType( config ) ) {
            return;
        }

        if ( !Arrays.asList( this.configuration.getAcceptType( config ) ).contains( contentType ) ) {
            throw new UploadException( "Unsupported type of provided file" );
        }
    }


    /**
     * Listen event TRANSACTION_SUCCESS and upload when transaction is synchronized
     *
     * @param event
     * @param params
     * @throws RuntimeException
     */
    @Override
    public void receiveEvent( final String event, final Map< String, Object > params ) throws RuntimeException {

        for ( final Map.Entry< UploadedFile, String > entry : this.triggers.entrySet() ) {
            this.configuration.getMoveRule( entry.getValue() ).move( entry.getKey() );
        }
    }


    @Override
    public int getPriority() {
        return 0;
    }
}
