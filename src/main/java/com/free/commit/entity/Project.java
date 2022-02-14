package com.free.commit.entity;

import com.free.commit.api.json.annotation.Group;
import com.free.commit.api.json.annotation.Json;
import com.free.commit.api.json.annotation.JsonPut;
import com.free.commit.api.json.annotation.Row;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.json.put.PutLastBuild;
import com.free.commit.configuration.response.Message;
import com.free.commit.exception.HttpUnprocessableEntityException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@JsonPut( groups = {
        @Group( name = GroupType.ADMIN, row = @Row( key = "last_build", handler = PutLastBuild.class ) ),
        @Group( name = GroupType.DEVELOPER, row = @Row( key = "last_build", handler = PutLastBuild.class ) )
} )
@Entity
public class Project {

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
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( columnDefinition = "TEXT" )
    private String description;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( nullable = false )
    private String repository;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( nullable = false )
    private String branch;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "spec_file_path", nullable = false )
    private String specFilePath;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    private Integer keepNumberBuild;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "allow_concurrent_execution", nullable = false )
    private boolean allowConcurrentExecution;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToOne( cascade = {CascadeType.PERSIST} )
    @JoinColumn( name = "repository_credential_id" )
    private Credential repositoryCredential;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @OneToMany( cascade = {CascadeType.PERSIST}, mappedBy = "project" )
    private final List< Secret > secrets;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @OneToMany( cascade = {CascadeType.PERSIST}, mappedBy = "project" )
    private final List< Build > builds;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToMany()
    @JoinTable(
            name = "project_developer",
            joinColumns = @JoinColumn( name = "project_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn( name = "developer_id", referencedColumnName = "id" )
    )
    private final List< Developer > developers;


    public Project() {
        secrets    = new ArrayList<>();
        builds     = new ArrayList<>();
        developers = new ArrayList<>();
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Project setName( String name ) {
        if ( name == null || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.PROJECT_NAME_REQUIRED );
        }

        this.name = name;

        return this;
    }


    public String getDescription() {
        return description;
    }


    public Project setDescription( String description ) {
        this.description = description;

        return this;
    }


    public String getRepository() {
        return repository;
    }


    public Project setRepository( String repository ) {
        if ( repository == null || repository.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.PROJECT_REPOSITORY_REQUIRED );
        }

        this.repository = repository;

        return this;
    }


    public String getBranch() {
        return branch;
    }


    public Project setBranch( String branch ) {
        if ( branch == null || branch.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.PROJECT_BRANCH_REQUIRED );
        }

        this.branch = branch;

        return this;
    }


    public String getSpecFilePath() {
        return specFilePath;
    }


    public Project setSpecFilePath( String specFilePath ) {
        if ( specFilePath == null || specFilePath.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.PROJECT_SPEC_FILE_PATH_REQUIRED );
        }

        this.specFilePath = specFilePath;

        return this;
    }


    public Integer getKeepNumberBuild() {
        return keepNumberBuild;
    }


    public Project setKeepNumberBuild( Integer keepNumberBuild ) {
        this.keepNumberBuild = keepNumberBuild;

        return this;
    }


    public boolean isAllowConcurrentExecution() {
        return allowConcurrentExecution;
    }


    public Project setAllowConcurrentExecution( Boolean allowConcurrentExecution ) {
        if ( allowConcurrentExecution == null ) {
            throw new HttpUnprocessableEntityException( Message.PROJECT_ALLOW_CONCURRENT_EXECUTION_REQUIRED );
        }

        this.allowConcurrentExecution = allowConcurrentExecution;

        return this;
    }


    public Credential getRepositoryCredential() {
        return repositoryCredential;
    }


    public Project setRepositoryCredential( Credential repositoryCredential ) {
        this.repositoryCredential = repositoryCredential;

        return this;
    }


    public List< Secret > getSecrets() {
        return secrets;
    }


    public Project addSecret( Secret secret ) {
        if ( !secrets.contains( secret ) ) {
            secrets.add( secret );

            if ( secret.getProject() != this ) {
                secret.setProject( this );
            }
        }

        return this;
    }


    public Project removeSecret( Secret secret ) {
        secrets.remove( secret );

        return this;
    }


    public List< Build > getBuilds() {
        return builds;
    }


    public Project addBuild( Build build ) {
        if ( !builds.contains( build ) ) {
            builds.add( build );

            if ( build.getProject() != this ) {
                build.setProject( this );
            }
        }

        return this;
    }


    public Project removeBuild( Build build ) {
        builds.remove( build );

        return this;
    }


    public List< Developer > getDevelopers() {
        return developers;
    }


    public Project addDeveloper( Developer developer ) {
        if ( !developers.contains( developer ) ) {
            developers.add( developer );

            if ( !developer.getProjects().contains( this ) ) {
                developer.addProject( this );
            }
        }


        return this;
    }


    public Project removeDeveloper( Developer developer ) {
        developers.remove( developer );

        return this;
    }
}
