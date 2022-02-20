package com.free.commit.parameter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface SecretParameter {
    String PREFIX   = "secret_";
    String NAME     = PREFIX + "name";
    String VALUE    = PREFIX + "value";
    String PROJECTS = PREFIX + "projects_id";
}
