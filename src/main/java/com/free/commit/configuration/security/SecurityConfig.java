package com.free.commit.configuration.security;

import com.free.commit.api.security.AuthenticationFilter;
import com.free.commit.api.security.JwtTokenHandler;
import com.free.commit.api.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( securedEnabled = true, prePostEnabled = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[]           WHITE_ENDPOINT = {
            "/auth",
            "/api/documentation/**",
            "/v2/api-docs",
            "/v3/api-docs"
    };
    protected            JwtTokenHandler    jwtTokenHandler;
    protected            UserDetailsService userDetailsService;
    protected            Security           security;


    public SecurityConfig(
            final JwtTokenHandler jwtTokenHandler,
            @Qualifier( "userDetailsService" ) final UserDetailsService userDetailsService,
            final Security security ) {
        this.jwtTokenHandler    = jwtTokenHandler;
        this.userDetailsService = userDetailsService;
        this.security           = security;
    }


    @Override
    protected void configure( final HttpSecurity http ) throws Exception {

        http.csrf().disable();

        http
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers( SecurityConfig.WHITE_ENDPOINT ).permitAll()
                .antMatchers( "/**" ).access( "hasRole('ROLE_ADMIN')" )
                .anyRequest().authenticated();

        http
                .addFilterBefore( this.authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class );
    }


    @Autowired
    public void configureGlobal( final AuthenticationManagerBuilder authenticationManagerBuilder ) throws Exception {
        authenticationManagerBuilder.userDetailsService( this.userDetailsService );
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public AuthenticationFilter authenticationTokenFilterBean() {
        return new AuthenticationFilter( this.jwtTokenHandler, this.security );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
