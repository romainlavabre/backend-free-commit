package com.free.commit.property;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ProjectProperty {
    String NAME                       = "name";
    String DESCRIPTION                = "description";
    String REPOSITORY                 = "repository";
    String BRANCH                     = "branch";
    String SPEC_FILE_PATH             = "specFilePath";
    String KEEP_NUMBER_BUILD          = "keepNumberBuild";
    String DEVELOPERS                 = "developers";
    String ALLOW_CONCURRENT_EXECUTION = "allowConcurrentExecution";
    String REPOSITORY_CREDENTIAL      = "repositoryCredential";
    String SECRETS                    = "secrets";
}
