package com.free.commit.api.request;

import java.util.List;
import java.util.Map;

public interface Request {

    /**
     * @param name Parameter name
     * @return TRUE if parameter name exists
     */
    boolean containsParameter( String name );


    /**
     * @param name Parameter name
     * @return Body parameter
     */
    Object getParameter( String name );


    /**
     * @param name Parameter name
     * @param type Parameter type
     * @return
     */
    < T > T getParameter( String name, Class< T > type );


    /**
     * @param name Parameter name
     * @param type Parameter type
     * @return
     */
    < T > T getParameter( String name, Class< T > type, boolean keepRawData );


    /**
     * Add or overwrite parameter
     *
     * @param name  Parameter name
     * @param value Parameter value
     */
    void setParameter( String name, Object value );


    /**
     * @param name Parameter name
     * @return List associate of parameter name
     */
    List< Object > getParameters( String name );


    /**
     * @return All the parameters contained in the request body
     */
    Map< String, Object > getAllParameters();


    /**
     * @param prefix Prefix of parameter name
     * @return All the parameters of the corp when the name of the parameter begins with the prefix
     */
    Map< String, Object > getAllParameters( String prefix );


    String getRawQueryString();


    /**
     * @param name Query string name
     * @return Query string associate to name, null otherwise
     */
    String getQueryString( String name );


    /**
     * Add or overwrite query string
     *
     * @param name  Query string name
     * @param value Query string value
     */
    void setQueryString( String name, String value );


    /**
     * @param name Parameter name
     * @return File associate to the name
     */
    UploadedFile getFile( String name );


    /**
     * @param name Parameter name
     * @return All Files associate to the name
     */
    List< UploadedFile > getFiles( String name );


    /**
     * Add or overwrite uploaded file
     *
     * @param name         Parameter name
     * @param uploadedFile Uploaded file
     */
    void setUploadedFile( String name, UploadedFile uploadedFile );


    /**
     * @return Client IP
     */
    String getClientIp();


    /**
     * @param name Header name
     * @return Header associate to the name
     */
    String getHeader( String name );


    /**
     * @return Content type
     */
    String getContentType();


    Integer getPort();


    String getHost();


    String getScheme();


    /**
     * @return Only uri <code>/path</code>
     */
    String getUri();


    /**
     * @return Base url <code>https://domain.ext<code/>
     */
    String getBaseUrl();


    /**
     * @return Request method
     */
    String getMethod();


    /**
     * @return Raw body
     */
    String getBody();
}
