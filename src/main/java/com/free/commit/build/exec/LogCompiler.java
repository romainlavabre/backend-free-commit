package com.free.commit.build.exec;

import com.free.commit.entity.Build;
import com.free.commit.entity.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogCompiler {

    /**
     * Get the log, separate by step
     *
     * @param process
     * @param build
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected static int compile( Process process, Build build ) throws IOException, InterruptedException {
        BufferedReader readerIn = new BufferedReader(
                new InputStreamReader( process.getInputStream() ) );

        Log currentLog = new Log( "init" );
        build.addLog( currentLog );

        String lineIn;
        while ( ( lineIn = readerIn.readLine() ) != null ) {
            if ( lineIn.equals( "null" ) || lineIn.isBlank() ) {
                continue;
            }

            if ( lineIn.startsWith( "Step @" ) ) {
                currentLog.close();
                currentLog.setSuccess( true );

                if ( lineIn.contains( "skipped" ) ) {
                    currentLog.setSkipped( true );
                } else {
                    String stepName = lineIn.replace( "Step @", "" ).replace( "...", "" ).trim();

                    currentLog = new Log( stepName );
                    build.addLog( currentLog );
                }
            }

            currentLog.addLine( lineIn );
        }


        int exitCode = process.waitFor();

        if ( exitCode == 0 ) {
            currentLog.close();
            return exitCode;
        }

        currentLog.setSuccess( false );
        currentLog.close();

        return exitCode;
    }
}
