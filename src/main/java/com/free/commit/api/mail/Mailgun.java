package com.free.commit.api.mail;

import com.free.commit.api.environment.Environment;
import com.free.commit.configuration.environment.Variable;
import kong.unirest.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Mailgun implements MailSender {

    protected Environment environment;


    public Mailgun( final Environment environment ) {
        this.environment = environment;
    }


    @Override
    public boolean send( final List< String > to, final String subject, final String message ) {
        final MultipartBody multipartBody = this.init();

        this.addRecipient( multipartBody, to )
            .addSubject( multipartBody, subject )
            .addMessage( multipartBody, message );

        final HttpResponse< JsonNode > response = multipartBody.asJson();


        return response.isSuccess();
    }


    @Override
    public boolean send( final List< String > to, final String subject, final String message, final List< File > files ) {
        final MultipartBody multipartBody = this.init();

        this.addRecipient( multipartBody, to )
            .addSubject( multipartBody, subject )
            .addFile( multipartBody, files )
            .addMessage( multipartBody, message );

        final HttpResponse< JsonNode > response = multipartBody.asJson();

        return response.isSuccess();
    }


    @Override
    public boolean send( final String to, final String subject, final String message ) {
        final MultipartBody multipartBody = this.init();

        this.addRecipient( multipartBody, to )
            .addSubject( multipartBody, subject )
            .addMessage( multipartBody, message );

        final HttpResponse< JsonNode > response = multipartBody.asJson();

        return response.isSuccess();
    }


    @Override
    public boolean send( final String to, final String subject, final String message, final List< File > files ) {
        final MultipartBody multipartBody = this.init();

        this.addRecipient( multipartBody, to )
            .addSubject( multipartBody, subject )
            .addFile( multipartBody, files )
            .addMessage( multipartBody, message );

        final HttpResponse< JsonNode > response = multipartBody.asJson();

        return response.isSuccess();
    }


    /**
     * Create request
     *
     * @return
     */
    protected MultipartBody init() {

        final HttpRequestWithBody requestWithBody =
                Unirest.post( "https://api.mailgun.net/v3/" + this.environment.getEnv( Variable.MAIL_DOMAIN ) + "/messages" );

        return requestWithBody
                .basicAuth( "api", this.environment.getEnv( Variable.MAIL_PRIVATE_KEY ) )
                .field( "from", this.environment.getEnv( Variable.MAIL_FROM ) )
                .field( "o:require-tls", "true" )
                .field( "o:skip-verification", "false" )
                .field( "encoding", "utf-8" );
    }


    /**
     * Add multiple recipients
     *
     * @param multipartBody
     * @param recipients
     * @return
     */
    protected Mailgun addRecipient( final MultipartBody multipartBody, final List< String > recipients ) {
        for ( final String recipient : recipients ) {
            multipartBody.field( "to", recipient );
        }

        return this;
    }


    /**
     * Add recipient
     *
     * @param multipartBody
     * @param recipient
     * @return
     */
    protected Mailgun addRecipient( final MultipartBody multipartBody, final String recipient ) {

        multipartBody.field( "to", recipient );

        return this;
    }


    /**
     * Add subject to mail
     *
     * @param multipartBody
     * @param subject
     * @return
     */
    protected Mailgun addSubject( final MultipartBody multipartBody, final String subject ) {
        multipartBody.field( "subject", subject );

        return this;
    }


    /**
     * @param multipartBody
     * @param message       Message on HTML or TEXT
     * @return
     */
    protected Mailgun addMessage( final MultipartBody multipartBody, final String message ) {
        if ( message.contains( "<html>" ) ) {
            multipartBody.field( "html", message );
        } else {
            multipartBody.field( "text", message );
        }

        return this;
    }


    /**
     * @param multipartBody
     * @param files         List of attachment
     * @return
     */
    protected Mailgun addFile( final MultipartBody multipartBody, final List< File > files ) {

        for ( final File file : files ) {
            multipartBody.field( "attachment", file );
        }

        return this;
    }
}
