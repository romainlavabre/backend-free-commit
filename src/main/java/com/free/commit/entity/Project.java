package com.free.commit.entity;

import com.free.commit.configuration.response.Message;
import com.free.commit.exception.HttpUnprocessableEntityException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Project {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column( nullable = false )
    private String name;

    @Column( columnDefinition = "TEXT" )
    private String description;

    @Column( nullable = false )
    private String repository;

    @Column( nullable = false )
    private String branch;

    @Column( name = "spec_file_path", nullable = false )
    private String specFilePath;

    private Integer keepNumberBuild;

    @ManyToOne( cascade = {CascadeType.PERSIST} )
    @JoinColumn( name = "repository_credential_id" )
    private Credential repositoryCredential;

    @OneToMany( cascade = {CascadeType.PERSIST}, mappedBy = "project" )
    private final List< Secret > secrets;

    @OneToMany( cascade = {CascadeType.PERSIST}, mappedBy = "project" )
    private final List< Build > builds;


    public Project() {
        secrets = new ArrayList<>();
        builds  = new ArrayList<>();
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
}
