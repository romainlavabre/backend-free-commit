package com.replace.replace.api.event;

import com.replace.replace.api.event.annotation.Subscribers;
import com.replace.replace.api.event.annotation.UnitEvent;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.upload.UploadHandler;
import com.replace.replace.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventConfiguration implements Event {

    protected UploadHandler  uploadHandler;
    protected HistoryHandler historyHandler;

    public EventConfiguration(
            UploadHandler uploadHandler,
            HistoryHandler historyHandler ) {
        this.uploadHandler  = uploadHandler;
        this.historyHandler = historyHandler;
    }

    @UnitEvent( name = TRANSACTION_SUCCESS )
    public Map< String, Class > transactionSuccess() {
        return new HashMap<>();
    }

    @Subscribers( event = TRANSACTION_SUCCESS )
    public List< EventSubscriber > subscribersTransactionSuccess() {
        List< EventSubscriber > list = new ArrayList<>();

        list.add( this.uploadHandler );
        list.add( this.historyHandler );

        return list;
    }

    @UnitEvent( name = PRODUCT_NAME_ALTERED )
    public Map< String, Class > productNameAltered() {
        Map< String, Class > map = new HashMap<>();

        map.put( Request.class.getName(), Request.class );
        map.put( Product.class.getName(), Product.class );

        return map;
    }

}
