package com.free.commit.api.sms;

import com.free.commit.api.environment.Environment;
import com.free.commit.configuration.environment.Variable;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Twilio implements SmsSender {

    protected Environment environment;


    public Twilio( final Environment environment ) {
        this.environment = environment;
    }


    @Override
    public boolean send( final String to, final String message ) {

        return this.core( to, message );
    }


    @Override
    public Boolean[] send( final List< String > to, final String message ) {
        final Boolean[] results = new Boolean[ to.size() ];

        int i = 0;

        for ( final String recipient : to ) {
            results[ i ] = this.core( recipient, message );

            i++;
        }

        return results;
    }


    protected boolean core( final String to, final String message ) {
        assert to != null && !to.isBlank() : "variable to should not be null or blank";

        com.twilio.Twilio.init(
                this.environment.getEnv( Variable.SMS_TWILIO_SID ),
                this.environment.getEnv( Variable.SMS_PRIVATE_KEY )
        );


        Message
                .creator(
                        new PhoneNumber( to ),
                        new PhoneNumber( this.environment.getEnv( Variable.SMS_FROM ) ),
                        message
                )
                .create();

        return true;
    }
}
