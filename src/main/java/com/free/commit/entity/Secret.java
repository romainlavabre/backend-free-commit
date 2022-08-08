package com.free.commit.entity;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.response.Message;
import com.free.commit.entity.encrypt.EncryptField;
import com.free.commit.exception.HttpUnprocessableEntityException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Convert( converter = EncryptField.class )
    @Column( nullable = false, columnDefinition = "TEXT" )
    private String value;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    private String escapeChar;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToMany( cascade = {CascadeType.PERSIST} )
    @JoinTable(
            name = "secret_project",
            joinColumns = @JoinColumn( name = "secret_id" ),
            inverseJoinColumns = @JoinColumn( name = "project_id" )
    )
    private final List< Project > projects;


    public Secret() {
        projects = new ArrayList<>();
    }


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


    public String getEscapeChar() {
        return escapeChar;
    }


    public Secret setEscapeChar( String escapeChar ) {
        this.escapeChar = escapeChar;

        return this;
    }


    public List< Project > getProjects() {
        return projects;
    }


    public Secret addProject( Project project ) {
        if ( !projects.contains( project ) ) {
            projects.add( project );

            if ( !project.getSecrets().contains( this ) ) {
                project.addSecret( this );
            }
        }

        return this;
    }


    public Secret removeProject( Project project ) {
        projects.remove( project );

        return this;
    }


    public boolean isGlobal() {
        return projects.isEmpty();
    }
}
