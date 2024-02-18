package com.free.commit.configuration.json;

import jakarta.persistence.Entity;
import org.romainlavabre.encoder.config.EncoderConfigurer;
import org.romainlavabre.encoder.config.FieldFormat;
import org.romainlavabre.encoder.overwritter.Overwrite;
import org.romainlavabre.encoder.put.Put;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigureEncoder {

    protected final List< Put >       puts;
    protected final List< Overwrite > overwrites;


    public ConfigureEncoder( List< Put > puts, List< Overwrite > overwrites ) {
        this.puts       = puts;
        this.overwrites = overwrites;
        configure();
    }


    public void configure() {
        EncoderConfigurer encoderConfigurer =
                EncoderConfigurer
                        .init()
                        .setEntityAnnotationDetector( Entity.class )
                        .setFieldFormat( FieldFormat.SNAKE_CASE );

        for ( Put put : puts ) {
            encoderConfigurer.addPut( put );
        }

        for ( Overwrite overwrite : overwrites ) {
            encoderConfigurer.addOverwrite( overwrite );
        }

        encoderConfigurer.build();
    }
}
