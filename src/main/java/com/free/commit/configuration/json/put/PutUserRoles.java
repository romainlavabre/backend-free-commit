package com.free.commit.configuration.json.put;

import com.free.commit.api.json.put.Put;
import com.free.commit.entity.Developer;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PutUserRoles implements Put< Developer > {

    @Override
    public Object build( Developer developer ) {
        return developer.getUser().getRoles();
    }
}
