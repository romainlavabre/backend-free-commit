package com.free.commit.api.upload;

import com.free.commit.api.event.EventSubscriber;
import com.free.commit.api.request.UploadedFile;
import com.free.commit.api.upload.exception.UploadException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface UploadHandler extends EventSubscriber {

    /**
     * Upload a received file base on configuration
     *
     * @param uploadedFile The file uploaded
     * @param config       Configuration name
     * @return TRUE if file uploaded, FALSE else
     */
    boolean upload( UploadedFile uploadedFile, String config )
            throws UploadException;
}
