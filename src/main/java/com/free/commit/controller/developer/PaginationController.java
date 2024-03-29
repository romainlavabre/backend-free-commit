package com.free.commit.controller.developer;

import com.free.commit.configuration.json.GroupType;
import com.free.commit.configuration.pagination.DeveloperPagination;
import com.free.commit.configuration.pagination.ProjectPagination;
import com.free.commit.configuration.pagination.SecretPagination;
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
@RestController( "DeveloperPaginationController" )
@RequestMapping( path = "/developer" )
public class PaginationController {

    protected final PaginationHandler paginationHandler;
    protected final Request           request;


    public PaginationController( PaginationHandler paginationHandler, Request request ) {
        this.paginationHandler = paginationHandler;
        this.request           = request;
    }


    @GetMapping( path = "/paginations/project" )
    public ResponseEntity< Map< String, Object > > project()
            throws NotSupportedKey, NotSupportedValue, NotSupportedOperator, FileError, NotSupportedDtoType {
        Pagination pagination = paginationHandler.getResult( request, ProjectPagination.class );
        return ResponseEntity.ok( pagination.encode( GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/paginations/developer" )
    public ResponseEntity< Map< String, Object > > developer()
            throws NotSupportedKey, NotSupportedValue, NotSupportedOperator, FileError, NotSupportedDtoType {
        Pagination pagination = paginationHandler.getResult( request, DeveloperPagination.class );
        return ResponseEntity.ok( pagination.encode( GroupType.DEVELOPER ) );
    }


    @GetMapping( path = "/paginations/secret" )
    public ResponseEntity< Map< String, Object > > secret()
            throws NotSupportedKey, NotSupportedValue, NotSupportedOperator, FileError, NotSupportedDtoType {
        Pagination pagination = paginationHandler.getResult( request, SecretPagination.class );
        return ResponseEntity.ok( pagination.encode( GroupType.DEVELOPER ) );
    }
}
