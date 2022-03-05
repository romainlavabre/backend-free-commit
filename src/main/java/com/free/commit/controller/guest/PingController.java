package com.free.commit.controller.guest;

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

    @GetMapping( path = "/ping" )
    public ResponseEntity< Void > ping() {
        return ResponseEntity.noContent().build();
    }
}
