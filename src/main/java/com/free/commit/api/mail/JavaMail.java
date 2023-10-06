package com.free.commit.api.mail;

import com.free.commit.api.environment.Environment;
import com.free.commit.configuration.environment.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class JavaMail implements MailSender {
    private static final Logger logger = LoggerFactory.getLogger( JavaMail.class );

    protected final Environment environment;


    public JavaMail( Environment environment ) {
        this.environment = environment;
    }


    @Override
    public boolean send( List< String > to, String subject, String message ) {
        boolean status = true;

        for ( String recipient : to ) {
            boolean unitStatus = send( recipient, subject, message );

            if ( !unitStatus ) {
                status = false;
            }
        }

        return status;
    }


    @Override
    public boolean send( List< String > to, String subject, String message, List< File > files ) {
        logger.warn( "Attachment not implemented for email" );
        return send( to, subject, message );
    }


    @Override
    public boolean send( String to, String subject, String message ) {
        MimeMessage msg = new MimeMessage( getSession() );

        try {
            msg.addHeader( "Content-type", "text/plain; charset=UTF-8" );
            msg.addHeader( "format", "flowed" );
            msg.addHeader( "Content-Transfer-Encoding", "8bit" );

            msg.setFrom( new InternetAddress( environment.getEnv( Variable.MAIL_FROM ), "Free Commit" ) );
            msg.setReplyTo( InternetAddress.parse( environment.getEnv( Variable.MAIL_FROM ), false ) );
            msg.setSubject( subject, "UTF-8" );
            msg.setText( message, "UTF-8" );
            msg.setRecipients( Message.RecipientType.TO, InternetAddress.parse( to, false ) );
            Transport.send( msg );

            return true;
        } catch ( UnsupportedEncodingException | MessagingException e ) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean send( String to, String subject, String message, List< File > files ) {
        logger.warn( "Attachment not implemented for email" );
        return send( to, subject, message );
    }


    protected Session getSession() {
        Properties props = new Properties();
        props.put( "mail.smtp.host", environment.getEnv( Variable.MAIL_HOST ) );
        props.put( "mail.smtp.port", environment.getEnv( Variable.MAIL_PORT ) );
        props.put( "mail.smtp.auth", "true" );
        props.put( "mail.smtp.starttls.enable", "true" );

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication( environment.getEnv( Variable.MAIL_FROM ), environment.getEnv( Variable.MAIL_PASSWORD ) );
            }
        };

        return Session.getInstance( props, auth );
    }
}
