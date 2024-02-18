package com.free.commit.view;

import com.free.commit.configuration.json.GroupType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.pagination.annotation.ModeType;
import org.romainlavabre.pagination.annotation.Pagination;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Pagination( mode = ModeType.FILE, filePath = "sql/developer.sql" )
@Entity
public class DeveloperView {

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    @Id
    private Long developer_id;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String user_username;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String developer_email;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String user_role;
}
