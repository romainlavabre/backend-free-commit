package com.free.commit.module.executor;

import com.free.commit.entity.Executor;
import com.free.commit.parameter.ExecutorParameter;
import com.free.commit.repository.ExecutorRepository;
import org.romainlavabre.crud.Update;
import org.romainlavabre.request.Request;
import org.springframework.stereotype.Service;

@Service( "updateExecutorName" )
public class UpdateName implements Update< Executor > {
    protected final ExecutorRepository executorRepository;


    public UpdateName( ExecutorRepository executorRepository ) {
        this.executorRepository = executorRepository;
    }


    @Override
    public void update( Request request, Executor executor ) {
        String name = request.getParameter( ExecutorParameter.NAME, String.class );

        executor.setName( name );

        executorRepository.persist( executor );
    }
}
