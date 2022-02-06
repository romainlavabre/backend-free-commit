package com.free.commit.api.sms;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface SmsSender {

    /**
     * @param to      Recipient
     * @param message Message
     * @return TRUE if sms sent
     */
    boolean send( String to, String message );

    /**
     * @param to      List of recipients
     * @param message Message
     * @return An array of results by recipient
     */
    Boolean[] send( List< String > to, String message );
}
