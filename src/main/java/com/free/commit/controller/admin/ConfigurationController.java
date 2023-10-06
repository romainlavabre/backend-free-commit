package com.free.commit.controller.admin;

import com.free.commit.api.environment.Environment;
import com.free.commit.api.mail.MailSender;
import com.free.commit.configuration.environment.Variable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RequestMapping( path = "/admin/config" )
@RestController( "AdminConfigurationController" )
public class ConfigurationController {

    protected final MailSender  mailSender;
    protected final Environment environment;


    public ConfigurationController( MailSender mailSender, Environment environment ) {
        this.mailSender  = mailSender;
        this.environment = environment;
    }


    @GetMapping( path = "/env" )
    public ResponseEntity< Map< String, Object > > getEnv() {
        Map< String, Object > env = new HashMap<>();

        env.put( Variable.MAX_PARALLEL_EXECUTOR, environment.getEnv( Variable.MAX_PARALLEL_EXECUTOR ) );
        env.put( Variable.DEFAULT_ADMIN_USERNAME, environment.getEnv( Variable.DEFAULT_ADMIN_USERNAME ) );
        env.put( Variable.JWT_LIFE_TIME, environment.getEnv( Variable.JWT_LIFE_TIME ) );
        env.put( Variable.MAIL_FROM, environment.getEnv( Variable.MAIL_FROM ) );
        env.put( Variable.MAIL_HOST, environment.getEnv( Variable.MAIL_HOST ) );
        env.put( Variable.MAIL_PORT, environment.getEnv( Variable.MAIL_PORT ) );

        return ResponseEntity.ok( env );
    }


    @PostMapping( path = "/mail/test/{recipient}" )
    public ResponseEntity< Void > testMail( @PathVariable( "recipient" ) String recipient ) {
        mailSender.send( recipient, "Free Commit - Test", "Test success" );

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .build();
    }
}
