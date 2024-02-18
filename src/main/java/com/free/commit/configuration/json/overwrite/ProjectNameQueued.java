package com.free.commit.configuration.json.overwrite;

import com.free.commit.build.BuildManager;
import org.romainlavabre.encoder.overwritter.Overwrite;
import org.springframework.stereotype.Service;

@Service
public class ProjectNameQueued implements Overwrite< BuildManager.Queued > {
    @Override
    public Object overwrite( BuildManager.Queued queued ) {
        return queued.getProject().getName();
    }
}
