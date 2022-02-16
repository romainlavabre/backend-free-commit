package com.free.commit.repository;

import com.free.commit.entity.Build;
import com.free.commit.entity.Project;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface BuildRepository extends DefaultRepository< Build > {
    List< Build > findAllByProject( Project project );
}
