package com.free.commit.module.developer;

import org.romainlavabre.crud.Update;
import org.romainlavabre.history.HistoryHandler;
import com.free.commit.entity.Developer;
import com.free.commit.parameter.DeveloperParameter;
import com.free.commit.property.DeveloperProperty;
import com.free.commit.repository.DeveloperRepository;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "updateDeveloperEmail" )
public class UpdateEmail implements Update< Developer > {

    protected final DeveloperRepository developerRepository;
    protected final HistoryHandler      historyHandler;


    public UpdateEmail(
            DeveloperRepository developerRepository,
            HistoryHandler historyHandler ) {
        this.developerRepository = developerRepository;
        this.historyHandler      = historyHandler;
    }


    @Override
    public void update( Request request, Developer developer ) {
        String email = ( String ) request.getParameter( DeveloperParameter.EMAIL );

        developer.setEmail( email );

        historyHandler.update( developer, DeveloperProperty.EMAIL );

        developerRepository.persist( developer );
    }
}
