package com.free.commit.configuration.startup;

import com.free.commit.configuration.environment.Variable;
import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Developer;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.security.PasswordEncoder;
import org.romainlavabre.security.User;
import org.romainlavabre.security.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DefaultAdmin {

    protected final Logger          logger = LoggerFactory.getLogger( getClass() );
    protected final UserRepository  userRepository;
    protected final Environment     environment;
    protected final EntityManager   entityManager;
    protected final PasswordEncoder passwordEncoder;


    public DefaultAdmin(
            UserRepository userRepository,
            Environment environment,
            EntityManager entityManager,
            PasswordEncoder passwordEncoder ) {
        this.userRepository  = userRepository;
        this.environment     = environment;
        this.entityManager   = entityManager;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @EventListener( ApplicationReadyEvent.class )
    public void createDefaultAdmin() {
        if ( userRepository.findAll().isEmpty() ) {
            User user = new User();
            user.setUsername( environment.getEnv( Variable.DEFAULT_ADMIN_USERNAME ) );
            user.setPassword( passwordEncoder.encode( environment.getEnv( Variable.DEFAULT_ADMIN_PASSWORD ) ) );
            user.addRole( Role.ADMIN );
            user.addRole( Role.DEVELOPER );

            Developer developer = new Developer();
            developer.setUser( user );

            entityManager.persist( user );
            entityManager.persist( developer );
            entityManager.flush();

            logger.info( "Admin " + user.getUsername() + " was created" );
        }
    }
}
