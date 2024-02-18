package com.free.commit.entity;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.json.overwrite.AscentUserToDeveloper;
import com.free.commit.configuration.json.put.PutUserRoles;
import com.free.commit.configuration.json.put.PutUserUsername;
import jakarta.persistence.*;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;
import org.romainlavabre.encoder.annotation.JsonPut;
import org.romainlavabre.encoder.annotation.Row;
import org.romainlavabre.security.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@JsonPut( groups = {
        @Group( name = GroupType.ADMIN, row = {
                @Row( key = "username", handler = PutUserUsername.class ),
                @Row( key = "roles", handler = PutUserRoles.class )
        } ),
        @Group( name = GroupType.DEVELOPER, row = {
                @Row( key = "username", handler = PutUserUsername.class ),
                @Row( key = "roles", handler = PutUserRoles.class )
        } )
} )
@Entity
public class Developer {

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
    @Column( name = "github_username" )
    private String githubUsername;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "gitlab_username" )
    private String gitlabUsername;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    private String email;

    @Json( groups = {
            @Group( name = GroupType.ADMIN, onlyId = false, ascent = true, overwrite = AscentUserToDeveloper.class ),
            @Group( name = GroupType.DEVELOPER, onlyId = false, ascent = true, overwrite = AscentUserToDeveloper.class )
    } )
    @OneToOne( cascade = { CascadeType.PERSIST } )
    @JoinColumn( name = "user_id", unique = true, nullable = false )
    private User user;

    @Json( groups = {
            @Group( name = GroupType.ADMIN ),
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToMany( mappedBy = "developers" )
    private final List< Project > projects;


    public Developer() {
        projects = new ArrayList<>();
    }


    public long getId() {
        return id;
    }


    public String getGithubUsername() {
        return githubUsername;
    }


    public Developer setGithubUsername( String githubUsername ) {
        this.githubUsername = githubUsername;

        return this;
    }


    public String getGitlabUsername() {
        return gitlabUsername;
    }


    public Developer setGitlabUsername( String gitlabUsername ) {
        this.gitlabUsername = gitlabUsername;

        return this;
    }


    public String getEmail() {
        return email;
    }


    public Developer setEmail( String email ) {
        this.email = email;

        return this;
    }


    public User getUser() {
        return user;
    }


    public Developer setUser( User user ) {
        this.user = user;

        return this;
    }


    public List< Project > getProjects() {
        return projects;
    }


    public Developer addProject( Project project ) {
        if ( !projects.contains( project ) ) {
            projects.add( project );

            if ( !project.getDevelopers().contains( this ) ) {
                project.addDeveloper( this );
            }
        }

        return this;
    }


    public Developer removeProject( Project project ) {
        projects.remove( project );

        project.removeDeveloper( this );

        return this;
    }


    public Developer cleaProjects() {
        for ( Project project : projects ) {
            project.removeDeveloper( this );
        }

        projects.clear();

        return this;
    }
}
