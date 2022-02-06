# SMS

The mail library permit to send sms, (the implementation is Twilio).

Service:

```java
com.replace.replace.api.sms.SmsSender;
```

All documentation is available on interface.


### Installation

You need to add this variables

```java
public interface Variable {
    String SMS_TWILIO_SID       = "sms.twilio.sid";
    String SMS_PRIVATE_KEY      = "sms.twilio.auth-token";
    String SMS_FROM             = "sms.from";
}
```

### Requirements

Maven dependency

```xml
<dependencies>
    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
        <version>8.9.0</version>
    </dependency>
</dependencies>
```

### Versions

##### 1.0.0

INITIAL