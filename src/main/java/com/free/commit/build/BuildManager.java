package com.free.commit.build;

import com.free.commit.entity.Project;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface BuildManager {

    String launch( Project project );


    String getLogs( String executorId );
}
