package com.free.commit.build;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface BuildManager {

    Queued launch( Project project, Initiator initiator );


    String getLogs( String executorId );


    List< Queued > getQueueds();


    List< Executed > getExecuteds();


    void kill( String executorId );


    class Executed {
        @Json( groups = {
                @Group
        } )
        private final Project project;

        private final Build build;

        @Json( groups = {
                @Group
        } )
        private final String executorId;

        private final Executor executor;

        private final Initiator initiator;


        public Executed(
                Project project,
                Build build,
                String executorId,
                Executor executor,
                Initiator initiator ) {
            this.project    = project;
            this.build      = build;
            this.executorId = executorId;
            this.executor   = executor;
            this.initiator  = initiator;
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
    }


    class Queued {
        @Json( groups = {
                @Group
        } )
        private final Project project;

        private final Build build;

        @Json( groups = {
                @Group
        } )
        private final String executorId;

        private final Initiator initiator;


        public Queued( Project project, Build build, String executorId, Initiator initiator ) {
            this.project    = project;
            this.build      = build;
            this.executorId = executorId;
            this.initiator  = initiator;
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
    }
}
