package com.free.commit.build;

import com.free.commit.build.exec.Executor;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.json.overwrite.ProjectNameExecuted;
import com.free.commit.configuration.json.overwrite.ProjectNameQueued;
import com.free.commit.entity.Build;
import com.free.commit.entity.Log;
import com.free.commit.entity.Project;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface BuildManager {

    Queued launch( Project project, Initiator initiator, String body );


    @Deprecated
    String getOutputLogs( String executorId );


    LogWrapper getLog( String executorId, String step, int lineNumber );


    Log getLog( long buildId, String step );


    List< Queued > getQueueds();


    List< Executed > getExecuteds();


    void killExecuted( String executorId );


    void killQueued( String executorId );


    class Executed {
        @Json( groups = {
                @Group( key = "project_name", overwrite = ProjectNameExecuted.class, onlyId = false )
        } )
        private final Project project;

        private final Build build;

        @Json( groups = {
                @Group
        } )
        private final String executorId;

        private final Executor executor;

        private final Initiator initiator;

        private final String requestBody;

        @Json( groups = {
                @Group
        } )
        private final ZonedDateTime at;


        public Executed(
                Project project,
                Build build,
                String executorId,
                Executor executor,
                Initiator initiator,
                String requestBody ) {
            this.project     = project;
            this.build       = build;
            this.executorId  = executorId;
            this.executor    = executor;
            this.initiator   = initiator;
            this.requestBody = requestBody;
            at               = ZonedDateTime.now( ZoneOffset.UTC );
        }


        public Project getProject() {
            return project;
        }


        public Build getBuild() {
            return build;
        }


        public String getExecutorId() {
            return executorId;
        }


        public Executor getExecutor() {
            return executor;
        }


        public Initiator getInitiator() {
            return initiator;
        }


        public String getRequestBody() {
            return requestBody;
        }
    }


    class Queued {
        @Json( groups = {
                @Group( key = "project_name", overwrite = ProjectNameQueued.class )
        } )
        private final Project project;

        private final Build build;

        @Json( groups = {
                @Group
        } )
        private final String executorId;

        private final Initiator initiator;

        private final String requestBody;


        public Queued( Project project, Build build, String executorId, Initiator initiator, String requestBody ) {
            this.project     = project;
            this.build       = build;
            this.executorId  = executorId;
            this.initiator   = initiator;
            this.requestBody = requestBody;
        }


        public Project getProject() {
            return project;
        }


        public Build getBuild() {
            return build;
        }


        public String getExecutorId() {
            return executorId;
        }


        public Initiator getInitiator() {
            return initiator;
        }


        public String getRequestBody() {
            return requestBody;
        }
    }

    class LogWrapper {
        @Json( groups = {
                @Group( name = GroupType.DEVELOPER )
        } )
        private final String step;

        @Json( groups = {
                @Group( name = GroupType.DEVELOPER )
        } )
        private final String log;

        @Json( groups = {
                @Group( name = GroupType.DEVELOPER )
        } )
        private final int lineNumber;

        @Json( groups = {
                @Group( name = GroupType.DEVELOPER )
        } )
        private ZonedDateTime startAt;

        @Json( groups = {
                @Group( name = GroupType.DEVELOPER )
        } )
        private ZonedDateTime closedAt;

        @Json( groups = {
                @Group( name = GroupType.DEVELOPER )
        } )
        private Boolean success;


        public LogWrapper( String step, String log, int lineNumber, ZonedDateTime startAt, ZonedDateTime closedAt, Boolean success ) {
            this.step       = step;
            this.log        = log;
            this.lineNumber = lineNumber;
            this.startAt    = startAt;
            this.closedAt   = closedAt;
            this.success    = success;
        }


        public String getLog() {
            return log;
        }


        public String getStep() {
            return step;
        }


        public int getLineNumber() {
            return lineNumber;
        }
    }
}
