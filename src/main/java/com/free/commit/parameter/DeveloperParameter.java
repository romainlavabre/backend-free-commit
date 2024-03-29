package com.free.commit.parameter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DeveloperParameter {
    String PREFIX          = "developer_";
    String USERNAME        = PREFIX + "username";
    String PASSWORD        = PREFIX + "password";
    String ROLE            = PREFIX + "roles";
    String GITHUB_USERNAME = PREFIX + "github_username";
    String GITLAB_USERNAME = PREFIX + "gitlab_username";
    String EMAIL           = PREFIX + "email";
    String ENABLED         = PREFIX + "enabled";
    String PROJECTS_ID     = PREFIX + "projects_id";
}
