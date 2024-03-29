package com.free.commit.configuration.json.overwrite;

import com.free.commit.configuration.security.Role;
import com.free.commit.entity.Project;
import org.romainlavabre.encoder.overwritter.Overwrite;
import org.romainlavabre.security.Security;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class OverwriteSignatureKey implements Overwrite< Project > {

    protected final Security security;


    public OverwriteSignatureKey( Security security ) {
        this.security = security;
    }


    @Override
    public Object overwrite( Project data ) {
        return security.hasRole( Role.ADMIN )
                ? data.getSignatureKey()
                : "***************";
    }
}
