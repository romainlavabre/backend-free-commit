package com.free.commit.view;

import com.free.commit.configuration.json.GroupType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.pagination.annotation.ModeType;
import org.romainlavabre.pagination.annotation.Pagination;

import java.time.ZonedDateTime;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Pagination( mode = ModeType.FILE, filePath = "sql/project.sql" )
@Entity
public class ProjectView {

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    @Id
    private Long project_id;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String project_name;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private Integer build_last_exit_code;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String build_last_exit_message;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private ZonedDateTime build_last_created_at;
}
