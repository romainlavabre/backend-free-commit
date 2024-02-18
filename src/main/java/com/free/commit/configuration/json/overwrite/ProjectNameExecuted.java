package com.free.commit.configuration.json.overwrite;

import com.free.commit.build.BuildManager;
import org.romainlavabre.encoder.overwritter.Overwrite;
import org.springframework.stereotype.Service;

@Service
public class ProjectNameExecuted implements Overwrite< BuildManager.Executed > {
    @Override
    public Object overwrite( BuildManager.Executed executed ) {
        return executed.getProject().getName();
    }
}
