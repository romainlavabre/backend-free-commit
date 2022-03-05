package com.free.commit.controller.admin;

import com.free.commit.api.mail.MailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RequestMapping( path = "/admin" )
@RestController( "AdminConfigurationController" )
public class ConfigurationController {

    protected final MailSender mailSender;


    public ConfigurationController( MailSender mailSender ) {
        this.mailSender = mailSender;
    }


    @PostMapping( path = "/config/mail/test/{recipient}" )
    public ResponseEntity< Void > testMail( @PathVariable( "recipient" ) String recipient ) {
        mailSender.send( recipient, "Free Commit - Test", "Test success" );

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .build();
    }
}
