package com.free.commit.api.pagination;

import com.free.commit.api.pagination.exception.NotSupportedKey;
import com.free.commit.api.pagination.exception.NotSupportedOperator;
import com.free.commit.api.pagination.exception.NotSupportedValue;
import com.free.commit.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PaginationBuilder {
    /**
     * @param request The request
     * @param dtoType The DTO to consume
     * @param view    The view name
     * @param <T>     The DTO type
     * @return Pagination object to encode
     * @throws NotSupportedOperator If an operator is invalid
     * @throws NotSupportedKey      If an key if invalid (Prevent SQL injection)
     * @throws NotSupportedValue    If a value is invalid (Prevent SQL injection)
     */
    < T > Pagination getResult( Request request, Class< T > dtoType, String view )
            throws NotSupportedOperator, NotSupportedKey, NotSupportedValue;
}
