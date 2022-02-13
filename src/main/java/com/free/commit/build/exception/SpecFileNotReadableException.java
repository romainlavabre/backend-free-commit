package com.free.commit.build.exception;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class SpecFileNotReadableException extends BuildException {

    public SpecFileNotReadableException() {
    }


    @Override
    public int getCode() {
        return 1001;
    }
}
