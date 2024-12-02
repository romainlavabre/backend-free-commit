package com.free.commit.controller.developer;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.pagination.SumDurationStatistic;
import org.romainlavabre.pagination.Pagination;
import org.romainlavabre.pagination.PaginationHandler;
import org.romainlavabre.pagination.exception.*;
import org.romainlavabre.request.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "DeveloperStatisticController" )
@RequestMapping( path = "/developer/statistics" )
public class StatisticController {

    protected final PaginationHandler paginationHandler;
    protected final Request           request;


    public StatisticController( PaginationHandler paginationHandler, Request request ) {
        this.paginationHandler = paginationHandler;
        this.request           = request;
    }


    @GetMapping( path = "/sum/duration" )
    public ResponseEntity< Map< String, Object > > sumDuration()
            throws FileError, NotSupportedDtoType, NotSupportedKey, NotSupportedOperator, NotSupportedValue {
        Pagination pagination = paginationHandler.getResult( request, SumDurationStatistic.class );
        return ResponseEntity.ok( pagination.encode( GroupType.DEVELOPER ) );
    }
}
