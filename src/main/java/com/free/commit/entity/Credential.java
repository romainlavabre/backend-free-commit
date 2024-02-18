package com.free.commit.entity;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.encrypt.EncryptField;
import jakarta.persistence.*;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.exception.HttpUnprocessableEntityException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Credential {

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( nullable = false )
    private String name;

    @Json( groups = {
            @Group( name = GroupType.ADMIN )
    } )
    @Convert( converter = EncryptField.class )
    @Column( name = "ssh_key", nullable = false, columnDefinition = "TEXT" )
    private String sshKey;


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Credential setName( String name ) {
        if ( name == null ) {
            throw new HttpUnprocessableEntityException( Message.CREDENTIAL_NAME_REQUIRED );
        }

        this.name = name;

        return this;
    }


    public String getSshKey() {
        return sshKey;
    }


    public Credential setSshKey( String sshKey ) {
        if ( sshKey == null || sshKey.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.CREDENTIAL_SSH_KEY_REQUIRED );
        }

        this.sshKey = sshKey;

        return this;
    }
}
