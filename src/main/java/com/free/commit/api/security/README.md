# SECURITY

Security component is extension of [spring security](https://spring.io/projects/spring-security) and the implementation 
inspired of [this repository](https://github.com/Kaway/jwt-auth).


For configure your access:

```java
com.replace.replace.configuration.security.WebSecurityConfig;
```

# Environment

- jwt.secret={512 bits key}

You can use this [generator](https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx)

# Generate password

You can use 

```java
PasswordEncoder.class
``` 

### Installation

You need to add the environment variables

```java
public class Variable{
    String JWT_SECRET       = "jwt.secret";
    String JWT_LIFE_TIME    = "jwt.life-time";
}
```

### Requirements

Maven dependency

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
        <version>2.4.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.2</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.2</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.2</version>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### Versions

##### 1.0.0

INITIAL

