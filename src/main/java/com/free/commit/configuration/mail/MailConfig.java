package com.free.commit.configuration.mail;

import com.free.commit.configuration.environment.Variable;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.mail.MailConfigurer;
import org.springframework.stereotype.Service;

@Service
public class MailConfig {
    protected final Environment environment;


    public MailConfig( Environment environment ) {
        this.environment = environment;
        configure();
    }


    public void configure() {
        MailConfigurer
                .init()
                .setSmtpHost( environment.getEnv( Variable.MAIL_HOST ) )
                .setSmtpPort( environment.getEnv( Variable.MAIL_PORT ) )
                .setSmtpUsername( environment.getEnv( Variable.MAIL_FROM ) )
                .setSmtpPassword( environment.getEnv( Variable.MAIL_PASSWORD ) )
                .build();
    }
}
