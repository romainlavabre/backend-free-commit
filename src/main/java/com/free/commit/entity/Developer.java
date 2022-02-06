package com.free.commit.entity;

import com.free.commit.api.security.User;

import javax.persistence.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Developer {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Column( name = "github_username" )
    private String githubUsername;

    private String email;

    @OneToOne( cascade = {CascadeType.PERSIST} )
    @JoinColumn( name = "user_id", unique = true, nullable = false )
    private User user;


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
}
