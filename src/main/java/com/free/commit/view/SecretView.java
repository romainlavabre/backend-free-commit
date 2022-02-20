package com.free.commit.view;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.configuration.json.GroupType;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
@Table( name = "secret_pagination" )
@Immutable
public class SecretView {

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
