package com.free.commit.build;

import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.exception.HttpNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class BuildManagerImpl implements BuildManager {

    private final Map< String, Executor > executors = new HashMap<>();

    protected final ApplicationContext applicationContext;


    public BuildManagerImpl( ApplicationContext applicationContext ) {
        this.applicationContext = applicationContext;
    }


    @Override
    public String launch( Project project ) {
        Executor executor = applicationContext.getBean( Executor.class );

        Build build = new Build();

        Runnable runnable = () -> executor.execute( project, build );
        runnable.run();

        String id = UUID.randomUUID().toString();

        executors.put( id, executor );

        return id;
    }


    @Override
    public String getLogs( String executorId ) {
        if ( executors.containsKey( executorId ) ) {
            return executors.get( executorId ).getBuild().getOutput();
        }

        throw new HttpNotFoundException( Message.EXECUTOR_NOT_FOUND );
    }
}
