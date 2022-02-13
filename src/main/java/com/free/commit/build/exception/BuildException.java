package com.free.commit.build.exception;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public abstract class BuildException extends Throwable {

    public abstract int getCode();
}
