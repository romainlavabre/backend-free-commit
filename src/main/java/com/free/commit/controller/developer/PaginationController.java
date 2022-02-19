package com.free.commit.controller.developer;

import com.free.commit.api.pagination.Pagination;
import com.free.commit.api.pagination.PaginationBuilder;
import com.free.commit.api.pagination.exception.NotSupportedKey;
import com.free.commit.api.pagination.exception.NotSupportedOperator;
import com.free.commit.api.pagination.exception.NotSupportedValue;
import com.free.commit.api.request.Request;
import com.free.commit.configuration.json.GroupType;
import com.free.commit.view.ProjectView;
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

    protected final PaginationBuilder paginationBuilder;
    protected final Request           request;


    public PaginationController(
            PaginationBuilder paginationBuilder,
            Request request ) {
        this.paginationBuilder = paginationBuilder;
        this.request           = request;
    }


    @GetMapping( path = "/paginations/project" )
    public ResponseEntity< Map< String, Object > > project()
            throws NotSupportedKey, NotSupportedValue, NotSupportedOperator {
        Pagination pagination = paginationBuilder.getResult( request, ProjectView.class, "project_pagination" );
        return ResponseEntity.ok( pagination.encode( GroupType.DEVELOPER ) );
    }
}
