package com.free.commit.parameter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ProjectParameter {
    String PREFIX                     = "project_";
    String NAME                       = PREFIX + "name";
    String DESCRIPTION                = PREFIX + "description";
    String REPOSITORY                 = PREFIX + "repository";
    String BRANCH                     = PREFIX + "branch";
    String SPEC_FILE_PATH             = PREFIX + "spec_file_path";
    String KEEP_NUMBER_BUILD          = PREFIX + "keep_number_build";
    String DEVELOPERS_ID              = PREFIX + "developers_id";
    String ALLOW_CONCURRENT_EXECUTION = PREFIX + "allow_concurrent_execution";
}
