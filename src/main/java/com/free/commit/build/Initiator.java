package com.free.commit.build;

/**
 * This class is wrapper for security
 *
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Initiator {

    private final String email;

    private final boolean allowed;


    public Initiator( String email, boolean allowed ) {
        this.email   = email;
        this.allowed = allowed;
    }


    public String getEmail() {
        return email;
    }


    public boolean isAllowed() {
        return allowed;
    }
}
