package com.free.commit.api.security;

import com.free.commit.api.environment.Environment;
import com.free.commit.configuration.environment.Variable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class JwtTokenImpl implements JwtTokenHandler {

    protected Environment    environment;
    protected UserRepository userRepository;


    public JwtTokenImpl(
            final Environment environment,
            final UserRepository userRepository ) {
        this.environment    = environment;
        this.userRepository = userRepository;
    }


    @Override
    public String createToken( final UserDetails userDetails ) {
        final User user = this.userRepository.findByUsername( userDetails.getUsername() );

        return Jwts.builder()
                   .signWith( SignatureAlgorithm.HS512, this.environment.getEnv( Variable.JWT_SECRET ) )
                   .setExpiration( this.getExpiration() )
                   .setIssuedAt( new Date() )
                   .setSubject( String.valueOf( user.getId() ) )
                   .claim( "username", user.getUsername() )
                   .claim( "roles", String.join( ",", AuthorityUtils.authorityListToSet( userDetails.getAuthorities() ) ) )
                   .compact();
    }


    @Override
    public Jws< Claims > validateJwtToken( final String token ) {
        return Jwts.parser().setSigningKey( this.environment.getEnv( Variable.JWT_SECRET ) ).parseClaimsJws( token );
    }


    protected Date getExpiration() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add( Calendar.SECOND, Integer.parseInt( this.environment.getEnv( Variable.JWT_LIFE_TIME ) ) );

        return calendar.getTime();
    }
}
