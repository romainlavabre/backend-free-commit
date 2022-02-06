package com.free.commit.module.developer;

import com.free.commit.api.crud.Update;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.repository.DeveloperRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperUsername" )
public class UpdateUsername implements Update< Developer > {

    protected final DeveloperRepository developerRepository;


    public UpdateUsername(
            DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    @Override
    public void update( Request request, Developer developer ) {
        String username = ( String ) request.getParameter( DeveloperParameter.USERNAME );

        developer.getUser().setUsername( username );

        developerRepository.persist( developer );
    }
}
