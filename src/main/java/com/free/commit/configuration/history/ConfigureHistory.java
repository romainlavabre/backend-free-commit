package com.free.commit.configuration.history;

import com.free.commit.configuration.event.Event;
import org.romainlavabre.history.HistoryConfigurer;
import org.romainlavabre.history.HistoryDataProvider;
import org.springframework.stereotype.Service;

@Service
public class ConfigureHistory {

    protected final HistoryDataProvider historyDataProvider;


    public ConfigureHistory( HistoryDataProvider historyDataProvider ) {
        this.historyDataProvider = historyDataProvider;
        configure();
    }


    private void configure() {
        HistoryConfigurer
                .init()
                .setHistoryDataProvider( historyDataProvider )
                .setOnTransactionSuccessEvent( Event.TRANSACTION_SUCCESS )
                .build();
    }
}
