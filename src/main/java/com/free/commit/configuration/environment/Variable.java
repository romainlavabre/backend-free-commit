package com.free.commit.configuration.environment;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Variable {
    String JWT_SECRET             = "jwt.secret";
    String JWT_LIFE_TIME          = "jwt.life-time";
    String SMS_TWILIO_SID         = "sms.twilio.sid";
    String SMS_PRIVATE_KEY        = "sms.twilio.auth-token";
    String SMS_FROM               = "sms.from";
    String BASE_TEMPLATE_PATH     = "base.template.path";
    String PDF_TMP_DIRECTORY      = "pdf.tmp.directory";
    String MAIL_HOST              = "mail.host";
    String MAIL_PORT              = "mail.port";
    String MAIL_FROM              = "mail.from";
    String MAIL_PASSWORD          = "mail.password";
    String DEFAULT_ADMIN_USERNAME = "default.admin.username";
    String DEFAULT_ADMIN_PASSWORD = "default.admin.password";
    String ENCRYPTION_KEY         = "encryption.key";
}
