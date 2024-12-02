package com.free.commit.configuration.pagination;

import com.free.commit.configuration.json.GroupType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.pagination.annotation.ModeType;
import org.romainlavabre.pagination.annotation.Pagination;

/**
 * @author romain.lavabre@proton.me
 */
@Pagination( mode = ModeType.FILE, filePath = "classpath:sql/sum-duration.sql", allowSorting = false )
@Entity
public class SumDurationStatistic {


    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Id
    private String date;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    private Long duration;
}
