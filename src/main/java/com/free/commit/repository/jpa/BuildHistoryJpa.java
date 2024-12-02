package com.free.commit.repository.jpa;

import com.free.commit.entity.BuildHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author romain.lavabre@proton.me
 */
@Repository
public interface BuildHistoryJpa extends JpaRepository< BuildHistory, Long > {

    @Query(
            value = """
                        SELECT 
                        CONCAT(YEAR(BH.at),"-" ,MONTH(BH.at)) AS "DATE",
                        SUM(BH.duration)
                        FROM build_history BH
                        GROUP BY CONCAT(YEAR(BH.at),"-" ,MONTH(BH.at))
                    """,
            nativeQuery = true
    )
    Map< String, Long > sumDuration();
}
