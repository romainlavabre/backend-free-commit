package com.free.commit.entity;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.entity.converter.OutputConverter;
import jakarta.persistence.*;
import org.romainlavabre.encoder.annotation.Group;
import org.romainlavabre.encoder.annotation.Json;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
public class Log {

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    protected long id;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( nullable = false )
    protected String step;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Convert( converter = OutputConverter.class )
    @Column( columnDefinition = "LONGTEXT" )
    protected String log;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @ManyToOne( cascade = { CascadeType.ALL } )
    @JoinColumn( name = "build_id", nullable = false )
    protected Build build;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    @Column( nullable = false )
    protected final ZonedDateTime startAt;


    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    protected ZonedDateTime closedAt;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    protected Boolean success;

    @Json( groups = {
            @Group( name = GroupType.DEVELOPER )
    } )
    protected Boolean skipped;


    public Log() {
        startAt = ZonedDateTime.now( ZoneOffset.UTC );
    }


    public Log( String step ) {
        startAt   = ZonedDateTime.now( ZoneOffset.UTC );
        this.step = step;
    }


    public long getId() {
        return id;
    }


    public String getStep() {
        return step;
    }


    public String getLog() {
        return log;
    }


    public Log addLine( String line ) {
        if ( line == null || line.isBlank() ) {
            return this;
        }

        if ( log == null ) {
            log = line;
        } else {
            log += "\n" + line;
        }

        return this;
    }


    public Build getBuild() {
        return build;
    }


    public Log setBuild( Build build ) {
        this.build = build;

        if ( !build.getLogs().contains( this ) ) {
            build.addLog( this );
        }

        return this;
    }


    public ZonedDateTime getStartAt() {
        return startAt;
    }


    public ZonedDateTime getClosedAt() {
        return closedAt;
    }


    public void close() {
        closedAt = ZonedDateTime.now( ZoneOffset.UTC );
    }


    public boolean isClosed() {
        return closedAt != null;
    }


    public Boolean getSuccess() {
        return success;
    }


    public Log setSuccess( Boolean success ) {
        this.success = success;

        return this;
    }


    public Boolean getSkipped() {
        return skipped;
    }


    public Log setSkipped( Boolean skipped ) {
        this.skipped = skipped;

        return this;
    }
}

