package com.free.commit.build.exec;

import com.free.commit.build.Initiator;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;

import java.util.List;

public interface Executor {
    void execute( Project project, Build build, Initiator initiator, String requestBody, List< String > ignoreSteps );


    void kill();


    boolean isActive();
}
