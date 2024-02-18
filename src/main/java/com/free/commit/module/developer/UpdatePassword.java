package com.free.commit.module.developer;

import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.repository.DeveloperRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.request.Request;
import org.romainlavabre.security.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperPassword" )
public class UpdatePassword implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final PasswordEncoder     passwordEncoder;


    public UpdatePassword(
            DeveloperRepository developerRepository,
            PasswordEncoder passwordEncoder ) {
        this.developerRepository = developerRepository;
        this.passwordEncoder     = passwordEncoder;
    }


    @Override
    public void update( Request request, Developer developer ) {
        String password = ( String ) request.getParameter( DeveloperParameter.PASSWORD );

        developer.getUser().setPassword( passwordEncoder.encode( password ) );

        developerRepository.persist( developer );
    }
}
