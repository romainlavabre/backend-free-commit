package com.free.commit.api.request;

import java.util.Map;

/**
 * The values of this class are scalable with state of upload, so, the name will change if file is renamed.
 * The path if available when the file is uploaded
 */
public interface UploadedFile {


    String getName();


    void setName( String name );


    String getContentType();


    void setContentType( String contentType );


    int getSize();


    void setSize( int size );


    byte[] getContent();


    void setContent( byte[] content );


    String getPath();


    void setPath( String path );


    Map< String, Object > getInfos();


    void setInfos( Map< String, Object > infos );


    boolean hasInfos();
}
