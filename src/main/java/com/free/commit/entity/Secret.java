package com.free.commit.entity;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.response.Message;
import com.free.commit.exception.HttpUnprocessableEntityException;

import javax.persistence.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Secret {

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
    
    @Column( nullable = false, columnDefinition = "TEXT" )
    private String value;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToOne( cascade = {CascadeType.PERSIST} )
    @JoinColumn( name = "project_id" )
    private Project project;


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Secret setName( String name ) {
        if ( name == null || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.SECRET_NAME_REQUIRED );
        }

        this.name = name;

        return this;
    }


    public String getValue() {
        return value;
    }


    public Secret setValue( String value ) {
        if ( value == null || value.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.SECRET_VALUE_REQUIRED );
        }

        this.value = value;

        return this;
    }


    public Project getProject() {
        return project;
    }


    public Secret setProject( Project project ) {
        this.project = project;

        if ( !project.getSecrets().contains( this ) ) {
            project.addSecret( this );
        }

        return this;
    }


    public boolean isGlobal() {
        return project == null;
    }
}
