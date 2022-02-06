package com.free.commit.module.developer;

import com.free.commit.api.crud.Update;
import com.free.commit.api.request.Request;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.repository.DeveloperRepository;
import com.free.commit.util.Cast;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperEnabled" )
public class UpdateEnabled implements Update< Developer > {

    protected final DeveloperRepository developerRepository;


    public UpdateEnabled(
            DeveloperRepository developerRepository ) {
        this.developerRepository = developerRepository;
    }


    @Override
    public void update( Request request, Developer developer ) {
        Boolean enabled = Cast.getBoolean( request.getParameter( DeveloperParameter.ENABLED ) );

        developer.getUser().setEnabled( enabled );

        developerRepository.persist( developer );
    }
}
