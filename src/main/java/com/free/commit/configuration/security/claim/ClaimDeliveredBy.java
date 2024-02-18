package com.free.commit.configuration.security.claim;

import org.romainlavabre.security.User;
import org.romainlavabre.security.attribute.ClaimBuilder;
import org.springframework.stereotype.Service;

@Service
public class ClaimDeliveredBy implements ClaimBuilder {
    @Override
    public String name() {
        return "provider";
    }


    @Override
    public Object value( User user ) {
        return "FREE-COMMIT";
    }
}
