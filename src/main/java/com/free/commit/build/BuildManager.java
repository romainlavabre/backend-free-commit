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

    Queued launch( Project project );


    String getLogs( String executorId );


    List< Queued > getQueueds();


    List< Executed > getExecuteds();


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


        public Executed(
                Project project,
                Build build,
                String executorId,
                Executor executor ) {
            this.project    = project;
            this.build      = build;
            this.executorId = executorId;
            this.executor   = executor;
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


        public Queued( Project project, Build build, String executorId ) {
            this.project    = project;
            this.build      = build;
            this.executorId = executorId;
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
    }
}
