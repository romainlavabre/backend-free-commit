package com.free.commit.module.executor;

import com.free.commit.entity.Executor;
import com.free.commit.parameter.ExecutorParameter;
import com.free.commit.repository.ExecutorRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service( "updateExecutorVariables" )
public class UpdateVariables implements Update< Executor > {
    protected final ExecutorRepository executorRepository;


    public UpdateVariables( ExecutorRepository executorRepository ) {
        this.executorRepository = executorRepository;
    }


    @Override
    public void update( Request request, Executor executor ) {
        Map< String, Object > variables = ( Map< String, Object > ) request.getParameter( ExecutorParameter.VARIABLES );

        executor.getVariables().clear();

        if ( variables != null ) {
            for ( Map.Entry< String, Object > entry : variables.entrySet() ) {
                executor.addVariable( entry.getKey(), entry.getValue() );
            }
        }

        executorRepository.persist( executor );
    }
}
