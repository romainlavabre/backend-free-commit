package com.free.commit.configuration.json.put;

import com.free.commit.api.json.put.Put;
import com.free.commit.entity.Build;
import com.free.commit.entity.Project;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PutLastBuild implements Put< Project > {

    @Override
    public Object build( Project project ) {
        Build build = project.getBuilds().isEmpty()
                ? null
                : project.getBuilds().get( project.getBuilds().size() - 1 );

        Map< String, Object > result = new HashMap<>();

        if ( build != null ) {
            result.put( "exit_code", build.getExitCode() );
            result.put( "exit_message", build.getExitMessage() );
            result.put( "created_at", build.getCreatedAt() );

            return result;
        }

        result.put( "exit_code", null );
        result.put( "exit_message", null );
        result.put( "created_at", null );

        return result;
    }
}
