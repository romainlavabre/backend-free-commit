package com.free.commit.build;

import com.free.commit.api.environment.Environment;
import com.free.commit.configuration.environment.Variable;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import com.free.commit.exception.HttpNotFoundException;
import com.free.commit.util.Cast;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class BuildManagerImpl implements BuildManager {

    private final List< Executed > executeds = new ArrayList<>();
    private final List< Queued >   queueds   = new ArrayList<>();

    protected final ApplicationContext      applicationContext;
    protected final ExecutorService         executorService;
    protected final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    protected final Environment             environment;


    public BuildManagerImpl(
            ApplicationContext applicationContext,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            Environment environment ) {
        this.applicationContext      = applicationContext;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.environment             = environment;
        Integer maxParallelExecutor = Cast.getInteger( environment.getEnv( Variable.MAX_PARALLEL_EXECUTOR ) );

        if ( maxParallelExecutor == null ) {
            maxParallelExecutor = 2;
        }

        this.executorService = Executors.newFixedThreadPool( maxParallelExecutor );
        process();
    }


    @Override
    public Queued launch( Project project, Initiator initiator ) {

        String id = UUID.randomUUID().toString();

        Queued queued = new Queued( project, new Build(), id, initiator );

        queueds.add( queued );

        return queued;
    }


    @Override
    public String getLogs( String executorId ) {
        for ( Executed executed : executeds ) {
            if ( executed.getExecutorId().equals( executorId ) ) {
                return executed.getBuild().getOutput();
            }
        }

        for ( Queued queued : queueds ) {
            if ( queued.getExecutorId().equals( executorId ) ) {
                return "Waits for an executor to become available";
            }
        }

        throw new HttpNotFoundException( Message.EXECUTOR_NOT_FOUND );
    }


    @Override
    public List< Queued > getQueueds() {
        return queueds;
    }


    @Override
    public List< Executed > getExecuteds() {
        return executeds;
    }


    @Override
    public void killExecuted( String executorId ) {
        Executor executor = null;

        Iterator< Executed > executedIterator = executeds.iterator();

        while ( executedIterator.hasNext() ) {
            Executed executed = executedIterator.next();

            if ( executed.getExecutorId().equals( executorId ) ) {
                executor = executed.getExecutor();
            }
        }

        if ( executor != null ) {
            executor.kill();
            return;
        }

        throw new HttpNotFoundException( Message.EXECUTOR_NOT_FOUND );
    }


    @Override
    public void killQueued( String executorId ) {
        Iterator< Queued > queuedIterator = queueds.iterator();

        while ( queuedIterator.hasNext() ) {
            Queued queued = queuedIterator.next();

            if ( queued.getExecutorId().equals( executorId ) ) {
                queueds.remove( queued );
                return;
            }
        }

        throw new HttpNotFoundException( Message.EXECUTOR_NOT_FOUND );
    }


    protected void process() {
        CronTrigger cronTrigger = new CronTrigger( "*/5 * * * * *" );
        Integer maxParallelExecutor =
                environment.getEnv( Variable.MAX_PARALLEL_EXECUTOR ) == null
                        ? 2
                        : Cast.getInteger( environment.getEnv( Variable.MAX_PARALLEL_EXECUTOR ) );


        threadPoolTaskScheduler.schedule( () -> {

            executeds.removeIf( executed -> !executed.getExecutor().isActive() );


            Iterator< Queued > queuedIterator = queueds.iterator();

            while ( queuedIterator.hasNext() && executeds.size() < maxParallelExecutor ) {
                Queued queued = queuedIterator.next();

                if ( !queued.getProject().isAllowConcurrentExecution() ) {
                    boolean found = false;

                    for ( Executed executed : executeds ) {
                        if ( executed.getProject().getId() == queued.getProject().getId() ) {
                            found = true;
                            break;
                        }
                    }

                    if ( found ) {
                        continue;
                    }
                }

                Executor executor = applicationContext.getBean( Executor.class );

                executeds.add( new Executed( queued.getProject(), queued.getBuild(), queued.getExecutorId(), executor, queued.getInitiator() ) );

                executorService.execute( () -> executor.execute( queued.getProject(), queued.getBuild(), queued.getInitiator() ) );

                queuedIterator.remove();
            }
        }, cronTrigger );
    }
}
