package com.free.commit.api.upload.duplication;

import com.free.commit.api.request.UploadedFile;
import com.free.commit.api.upload.exception.DuplicationException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DuplicationRule {

    /**
     * You can rename this file or throw an DuplicationException
     *
     * @param uploadedFile
     */
    void exec( UploadedFile uploadedFile )
            throws DuplicationException;
}
