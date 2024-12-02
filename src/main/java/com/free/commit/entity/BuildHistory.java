package com.free.commit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;

@Entity
public class BuildHistory {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private String projectName;

    private long duration;

    private ZonedDateTime at;


    public long getId() {
        return id;
    }


    public String getProjectName() {
        return projectName;
    }


    public BuildHistory setProjectName( String projectName ) {
        this.projectName = projectName;
        return this;
    }


    public long getDuration() {
        return duration;
    }


    public BuildHistory setDuration( long duration ) {
        this.duration = duration;
        return this;
    }


    public ZonedDateTime getAt() {
        return at;
    }


    public BuildHistory setAt( ZonedDateTime at ) {
        this.at = at;
        return this;
    }
}
