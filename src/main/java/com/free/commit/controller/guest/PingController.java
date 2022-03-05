package com.free.commit.controller.guest;

import com.free.commit.api.mail.MailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RequestMapping( path = "/guest" )
@RestController( "GuestPingController" )
public class PingController {

    protected final MailSender mailSender;


    public PingController( MailSender mailSender ) {
        this.mailSender = mailSender;
    }


    @GetMapping( path = "/ping" )
    public ResponseEntity< Void > ping() {
        mailSender.send( "romainlavabre98@gmail.com", "Test", "Coucou" );
        return ResponseEntity.noContent().build();
    }
}
