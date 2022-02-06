package com.free.commit.api.request;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class UploadedFileImpl implements UploadedFile {

    private String                name;
    private String                contentType;
    private int                   size;
    private byte[]                content;
    private String                path;
    private Map< String, Object > infos;


    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void setName( final String name ) {
        this.name = name;
    }


    @Override
    public String getContentType() {
        return this.contentType;
    }


    @Override
    public void setContentType( final String contentType ) {
        this.contentType = contentType;
    }


    @Override
    public int getSize() {
        return this.size;
    }


    @Override
    public void setSize( final int size ) {
        this.size = size;
    }


    @Override
    public byte[] getContent() {
        return this.content;
    }


    @Override
    public void setContent( final byte[] content ) {
        this.content = content;
    }


    @Override
    public String getPath() {
        return this.path;
    }


    @Override
    public void setPath( final String path ) {
        this.path = path;
    }


    @Override
    public Map< String, Object > getInfos() {
        return this.infos;
    }


    @Override
    public void setInfos( final Map< String, Object > infos ) {
        this.infos = infos;
    }


    @Override
    public boolean hasInfos() {
        return this.infos != null;
    }
}
