package com.free.commit.configuration.pagination;

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
@Pagination( mode = ModeType.FILE, filePath = "classpath:sql/secret.sql" )
@Entity
public class SecretPagination {

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    @Id
    private Long secret_id;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String secret_name;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER ),
            @Group( name = GroupType.ADMIN )
    } )
    private String secret_scope;
}
