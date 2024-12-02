package com.free.commit.module.buildhistory;

import com.free.commit.entity.Build;
import com.free.commit.entity.BuildHistory;
import com.free.commit.entity.Log;
import jakarta.persistence.EntityManager;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author romain.lavabre@proton.me
 */
public class BuildHistoryBuilder {
    public static void build( Build build, EntityManager entityManager ) {
        BuildHistory buildHistory = new BuildHistory();

        long duration = 0;

        for ( Log log : build.getLogs() ) {
            duration += log.getClosedAt().toEpochSecond() - log.getStartAt().toEpochSecond();
        }

        buildHistory
                .setProjectName( build.getProject().getName() )
                .setAt( ZonedDateTime.now( ZoneOffset.UTC ) )
                .setDuration( duration );

        entityManager.persist( buildHistory );
    }
}
