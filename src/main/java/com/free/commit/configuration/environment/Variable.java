package com.free.commit.configuration.environment;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Variable {
    String JWT_SECRET         = "jwt.secret";
    String JWT_LIFE_TIME      = "jwt.life-time";
    String SMS_TWILIO_SID     = "sms.twilio.sid";
    String SMS_PRIVATE_KEY    = "sms.twilio.auth-token";
    String SMS_FROM           = "sms.from";
    String BASE_TEMPLATE_PATH = "base.template.path";
    String PDF_TMP_DIRECTORY  = "pdf.tmp.directory";
    String MAIL_DOMAIN        = "mail.domain";
    String MAIL_PRIVATE_KEY   = "mail.private.key";
    String MAIL_FROM          = "mail.from";
}
