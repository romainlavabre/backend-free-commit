package com.free.commit.configuration.json.put;

import com.free.commit.entity.Developer;
import org.romainlavabre.encoder.put.Put;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PutUserUsername implements Put< Developer > {

    @Override
    public Object build( Developer developer ) {
        return developer.getUser().getUsername();
    }
}
