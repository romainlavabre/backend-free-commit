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

    @Column( columnDefinition = "TEXT" )
    private String output;

    @Column( name = "exit_code", nullable = false )
    private int exitCode;

    @Column( name = "exit_message" )
    private String exitMessage;

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


    public String getOutput() {
        return output;
    }


    public Build addOutputLine( String line ) {
        output += "\n" + line;

        return this;
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
