package com.free.commit.module.executor;

import com.free.commit.entity.Executor;
import com.free.commit.parameter.ExecutorParameter;
import com.free.commit.repository.ExecutorRepository;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

@Service( "createExecutor" )
public class Create implements org.romainlavabre.crud.Create< Executor > {
    protected final ExecutorRepository executorRepository;


    public Create( ExecutorRepository executorRepository ) {
        this.executorRepository = executorRepository;
    }


    @Override
    public void create( Request request, Executor executor ) {
        String name   = request.getParameter( ExecutorParameter.NAME, String.class );
        String driver = request.getParameter( ExecutorParameter.DRIVER, String.class );

        executor
                .setName( name )
                .setDriver( driver );

        initDefaultVariables( executor );

        executorRepository.persist( executor );
    }


    protected void initDefaultVariables( Executor executor ) {
        if ( executor.getDriver().equals( Executor.DRIVER_OPEN_STACK ) ) {
            executor
                    .addVariable( "FLAVOR_NAME", "" )
                    .addVariable( "IMAGE_NAME", "" )
                    .addVariable( "SECURITY_GROUP_NAME", "" )
                    .addVariable( "NETWORK_NAME", "" )
                    .addVariable( "VOLUME_ID", "" );
        }
    }
}
