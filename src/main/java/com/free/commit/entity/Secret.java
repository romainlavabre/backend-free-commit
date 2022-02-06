package com.free.commit.entity;

import com.free.commit.configuration.response.Message;
import com.free.commit.exception.HttpUnprocessableEntityException;

import javax.persistence.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Entity
public class Secret {

    @Id
    private long id;

    @Column( nullable = false )
    private String name;

    private boolean encrypt;

    @ManyToOne( cascade = {CascadeType.PERSIST} )
    @JoinColumn( name = "project_id" )
    private Project project;


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Secret setName( String name ) {
        if ( name == null || name.isBlank() ) {
            throw new HttpUnprocessableEntityException( Message.SECRET_NAME_REQUIRED );
        }
        
        this.name = name;

        return this;
    }


    public boolean isEncrypt() {
        return encrypt;
    }


    public Secret setEncrypt( Boolean encrypt ) {
        if ( encrypt == null ) {
            throw new HttpUnprocessableEntityException( Message.SECRET_ENCRYPT_REQUIRED );
        }

        this.encrypt = encrypt;

        return this;
    }


    public Project getProject() {
        return project;
    }


    public Secret setProject( Project project ) {
        this.project = project;

        return this;
    }


    public boolean isGlobal() {
        return project == null;
    }
}
