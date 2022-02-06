package com.free.commit.entity;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Build {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private final ZonedDateTime createdAt;

    @ManyToOne( cascade = {CascadeType.PERSIST} )
    @JoinColumn( name = "project_id", nullable = false )
    private Project project;


    public Build() {
        createdAt = ZonedDateTime.now( ZoneId.of( "UTC" ) );
    }


    public long getId() {
        return id;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Project getProject() {
        return project;
    }


    public Build setProject( Project project ) {
        this.project = project;

        if ( !project.getBuilds().contains( this ) ) {
            project.addBuild( this );
        }

        return this;
    }
}
