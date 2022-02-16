package com.free.commit.repository.jpa;

import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface BuildJpa extends JpaRepository< Build, Long > {
    List< Build > findAllBuildByProject( Project project );
}
