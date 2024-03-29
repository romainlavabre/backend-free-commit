package com.free.commit.entity;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.converter.OutputConverter;
import jakarta.persistence.*;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Build {

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Convert( converter = OutputConverter.class )
    @Column( columnDefinition = "LONGTEXT" )
    private String output;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "exit_code", nullable = false )
    private Integer exitCode;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "exit_message" )
    private String exitMessage;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "created_at", nullable = false )
    private final ZonedDateTime createdAt;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( name = "completed_at", nullable = false )
    private ZonedDateTime completedAt;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToOne( cascade = { CascadeType.PERSIST } )
    @JoinColumn( name = "project_id", nullable = false )
    private Project project;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "build" )
    protected final List< Log > logs;


    public Build() {
        createdAt = ZonedDateTime.now( ZoneId.of( "UTC" ) );
        logs      = new ArrayList<>();
    }


    public long getId() {
        return id;
    }


    public String getOutput() {
        return output;
    }


    public Build addOutputLine( String line ) {
        if ( output == null ) {
            output = line;
        } else {
            output += "\n" + line;
        }

        return this;
    }


    public Integer getExitCode() {
        return exitCode;
    }


    public Build setExitCode( Integer exitCode ) {
        this.exitCode = exitCode;

        initCompletedAt();
        addExitCode();

        return this;
    }


    public String getExitMessage() {
        return exitMessage;
    }


    public Build setExitMessage( String exitMessage ) {
        this.exitMessage = exitMessage;

        addExitCode();

        return this;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public ZonedDateTime getCompletedAt() {
        return completedAt;
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


    protected void initCompletedAt() {
        completedAt = ZonedDateTime.now( ZoneId.of( "UTC" ) );
    }


    protected void addExitCode() {
        if ( exitCode != null && exitMessage != null ) {
            addOutputLine( "Exit with code " + exitCode + " and message " + exitMessage );
        }
    }


    public List< Log > getLogs() {
        return logs;
    }


    public Build addLog( Log log ) {
        if ( !logs.contains( log ) ) {
            logs.add( log );

            if ( log.getBuild() != this ) {
                log.setBuild( this );
            }
        }

        return this;
    }

}
