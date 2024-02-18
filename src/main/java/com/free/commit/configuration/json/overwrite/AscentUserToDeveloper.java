package com.free.commit.configuration.json.overwrite;

import com.free.commit.entity.Developer;
import org.romainlavabre.encoder.overwritter.Overwrite;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AscentUserToDeveloper implements Overwrite< Developer > {
    @Override
    public Object overwrite( Developer developer ) {
        return Map.of(
                "username", developer.getUser().getUsername(),
                "password", "***********",
                "enabled", developer.getUser().isEnabled()
        );
    }
}
