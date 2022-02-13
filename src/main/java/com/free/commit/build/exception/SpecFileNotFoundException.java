package com.free.commit.build.exception;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class SpecFileNotFoundException extends BuildException {

    public SpecFileNotFoundException() {
    }


    @Override
    public int getCode() {
        return 1000;
    }
}
