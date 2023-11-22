package com.free.commit.build;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.configuration.json.overwrite.ProjectNameExecuted;
import com.free.commit.configuration.json.overwrite.ProjectNameQueued;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface BuildManager {

    Queued launch( Project project, Initiator initiator, String body );


    String getLogs( String executorId );


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
}
