package com.free.commit.api.history;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class History {

    public static final String TYPE_CREATE = "CREATE";
    public static final String TYPE_UPDATE = "UPDATE";
    public static final String TYPE_DELETE = "DELETE";

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long      id;
    @Column( name = "author_type" )
    private String    authorType;
    @Column( name = "author_id" )
    private int       authorId;
    @Column( name = "subject_type", nullable = false )
    private String    subjectType;
    @Column( name = "subject_id", nullable = false )
    private int       subjectId;
    @Column( name = "subject_property" )
    private String    subjectProperty;
    @Column( name = "new_value", columnDefinition = "TEXT" )
    private String    newValue;
    @Column( name = "log_type", nullable = false )
    private String    logType;
    @Column( name = "ip_address" )
    private String    ipAddress;
    @Column( name = "created_at", nullable = false )
    private Timestamp createdAt;


    public History() {
        this.createdAt = new Timestamp( (new Date().getTime()) );
    }


    public long getId() {
        return this.id;
    }


    public void setId( final long id ) {
        this.id = id;
    }


    public String getAuthorType() {
        return this.authorType;
    }


    public void setAuthorType( final String authorType ) {
        this.authorType = authorType;
    }


    public int getAuthorId() {
        return this.authorId;
    }


    public void setAuthorId( final int authorId ) {
        this.authorId = authorId;
    }


    public String getSubjectType() {
        return this.subjectType;
    }


    public void setSubjectType( final String subjectType ) {
        this.subjectType = subjectType;
    }


    public int getSubjectId() {
        return this.subjectId;
    }


    public void setSubjectId( final int subjectId ) {
        this.subjectId = subjectId;
    }


    public String getSubjectProperty() {
        return this.subjectProperty;
    }


    public void setSubjectProperty( final String subjectProperty ) {
        this.subjectProperty = subjectProperty;
    }


    public String getNewValue() {
        return this.newValue;
    }


    public void setNewValue( final String newValue ) {
        this.newValue = newValue;
    }


    public String getLogType() {
        return this.logType;
    }


    public void setLogType( final String logType ) {
        this.logType = logType;
    }


    public String getIpAddress() {
        return this.ipAddress;
    }


    public void setIpAddress( final String ipAddress ) {
        this.ipAddress = ipAddress;
    }


    public Timestamp getCreatedAt() {
        return this.createdAt;
    }


    public void setCreatedAt( final Timestamp createdAt ) {
        this.createdAt = createdAt;
    }
}
