package com.free.commit.build;

import com.free.commit.build.exec.Executor;
import com.free.commit.build.exec.LocalExecutor;
import com.free.commit.build.exec.OpenStackExecutor;
import com.free.commit.configuration.environment.Variable;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.Build;
import com.free.commit.entity.Log;
import com.free.commit.entity.Project;
import com.free.commit.parameter.BuildParameter;
import com.free.commit.repository.BuildRepository;
import com.free.commit.util.Cast;
import org.romainlavabre.environment.Environment;
import org.romainlavabre.exception.HttpNotFoundException;
import org.romainlavabre.request.Request;
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
    protected final BuildRepository         buildRepository;


    public BuildManagerImpl(
            ApplicationContext applicationContext,
            ThreadPoolTaskScheduler threadPoolTaskScheduler,
            Environment environment, BuildRepository buildRepository ) {
        this.applicationContext      = applicationContext;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.environment             = environment;
        this.buildRepository         = buildRepository;
        Integer maxParallelExecutor = Cast.getInteger( environment.getEnv( Variable.MAX_PARALLEL_EXECUTOR ) );

        if ( maxParallelExecutor == null ) {
            maxParallelExecutor = 2;
        }

        this.executorService = Executors.newFixedThreadPool( maxParallelExecutor );
        process();
    }


    @Override
    public Queued launch( Project project, Initiator initiator, String body ) {
        return launch( project, initiator, body, new ArrayList<>() );
    }


    @Override
    public Queued launch( Project project, Initiator initiator, String body, Request request ) {
        List< String > ignoreSteps = new ArrayList<>();

        for ( Object step : request.getParameters( BuildParameter.IGNORE_STEPS ) ) {
            ignoreSteps.add( step.toString() );
        }

        return launch( project, initiator, body, ignoreSteps );
    }


    protected Queued launch( Project project, Initiator initiator, String body, List< String > ignoreSteps ) {
        String id = UUID.randomUUID().toString();

        Queued queued = new Queued( project, new Build(), id, initiator, body, ignoreSteps );

        queueds.add( queued );

        return queued;
    }


    @Override
    public String getOutputLogs( String executorId ) {
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
    public LogWrapper getLog( String executorId, String step, int lineNumber ) {
        for ( Executed executed : executeds ) {
            if ( !executed.getExecutorId().equals( executorId ) ) {
                continue;
            }

            if ( executed.getBuild().getLogs().isEmpty() ) {
                return new LogWrapper( "init", "Waiting for first byte", 0, null, null, null, null );
            }

            Log currentClientLog = null;
            int index            = 0;

            for ( Log log : executed.getBuild().getLogs() ) {
                if ( log.getStep().equals( step ) ) {
                    currentClientLog = log;
                    break;
                }

                index++;
            }

            if ( currentClientLog == null ) {
                currentClientLog = executed.getBuild().getLogs().get( 0 );
            }

            String[] lines = currentClientLog.getLog().split( "\\n" );

            if ( lines.length == lineNumber ) {
                if ( executed.getBuild().getLogs().size() - 1 > index ) {
                    Log log = executed.getBuild().getLogs().get( index + 1 );

                    return new LogWrapper( log.getStep(), log.getLog(), log.getLog().split( "\\n" ).length, log.getStartAt(), log.getClosedAt(), log.getSuccess(), log.getSkipped() );
                }

                return new LogWrapper( step, "", lines.length, currentClientLog.getStartAt(), currentClientLog.getClosedAt(), currentClientLog.getSuccess(), currentClientLog.getSkipped() );
            }

            String toReturn = null;


            for ( int i = lineNumber; i < lines.length; i++ ) {
                if ( toReturn == null ) {
                    toReturn = "\n" + lines[ i ];
                    continue;
                }

                toReturn += "\n" + lines[ i ];
            }

            return new LogWrapper( currentClientLog.getStep(), toReturn, lines.length, currentClientLog.getStartAt(), currentClientLog.getClosedAt(), currentClientLog.getSuccess(), currentClientLog.getSkipped() );
        }

        for ( Queued queued : queueds ) {
            if ( queued.getExecutorId().equals( executorId ) ) {
                return new LogWrapper( "init", "Waiting for first byte", 0, null, null, null, null );
            }
        }

        throw new HttpNotFoundException( Message.EXECUTOR_NOT_FOUND );
    }


    @Override
    public Log getLog( long buildId, String step ) {
        Build build = buildRepository.findOrFail( buildId );

        if ( build.getLogs().isEmpty() ) {
            throw new HttpNotFoundException( "LOG_NOT_FOUND" );
        }

        if ( step.equals( "-1" ) ) {
            return build.getLogs().get( 0 );
        }

        boolean found = false;

        for ( Log log : build.getLogs() ) {
            if ( log.getStep().equals( step ) ) {
                found = true;
                continue;
            }

            if ( found ) {
                return log;
            }
        }

        throw new HttpNotFoundException( "LOG_NOT_FOUND" );
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

                Executor executor;

                if ( queued.getProject().getExecutor() != null
                        && queued.getProject().getExecutor().getDriver().equals( com.free.commit.entity.Executor.DRIVER_OPEN_STACK ) ) {
                    executor = applicationContext.getBean( OpenStackExecutor.class );
                } else {
                    executor = applicationContext.getBean( LocalExecutor.class );

                }

                executeds.add( new Executed( queued.getProject(), queued.getBuild(), queued.getExecutorId(), executor, queued.getInitiator(), queued.getRequestBody() ) );

                executorService.execute( () -> executor.execute( queued.getProject(), queued.getBuild(), queued.getInitiator(), queued.getRequestBody(), queued.getIgnoreSteps() ) );

                queuedIterator.remove();
            }
        }, cronTrigger );
    }
}
