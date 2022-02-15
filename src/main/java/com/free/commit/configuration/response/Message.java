package com.free.commit.configuration.response;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Message {
    String SECRET_NAME_REQUIRED                        = "SECRET_NAME_REQUIRED";
    String SECRET_VALUE_REQUIRED                       = "SECRET_VALUE_REQUIRED";
    String PROJECT_NAME_REQUIRED                       = "PROJECT_NAME_REQUIRED";
    String PROJECT_REPOSITORY_REQUIRED                 = "PROJECT_REPOSITORY_REQUIRED";
    String PROJECT_BRANCH_REQUIRED                     = "PROJECT_BRANCH_REQUIRED";
    String PROJECT_SPEC_FILE_PATH_REQUIRED             = "PROJECT_SPEC_FILE_PATH_REQUIRED";
    String PROJECT_ALLOW_CONCURRENT_EXECUTION_REQUIRED = "PROJECT_ALLOW_CONCURRENT_EXECUTION_REQUIRED";
    String PROJECT_SIGNATURE_KEY_REQUIRED              = "PROJECT_SIGNATURE_KEY_REQUIRED";
    String CREDENTIAL_SSH_KEY_REQUIRED                 = "CREDENTIAL_SSH_KEY_REQUIRED";
    String CREDENTIAL_NAME_REQUIRED                    = "CREDENTIAL_NAME_REQUIRED";
    String INVALID_ENCRYPTION_KEY                      = "INVALID_ENCRYPTION_KEY";
    String EXECUTOR_NOT_FOUND                          = "EXECUTOR_NOT_FOUND";
    String WEBHOOK_SENDER_NOT_FOUND                    = "WEBHOOK_SENDER_NOT_FOUND";
}
