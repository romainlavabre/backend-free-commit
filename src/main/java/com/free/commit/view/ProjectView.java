package com.free.commit.view;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.configuration.json.GroupType;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
@Table( name = "project_pagination" )
@Immutable
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
