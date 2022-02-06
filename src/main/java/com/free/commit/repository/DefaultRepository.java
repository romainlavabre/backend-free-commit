package com.free.commit.repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DefaultRepository< T > {
    Optional< T > findById( long id );


    Optional< T > findById( long id, LockModeType lockModeType );


    T findOrFail( Long id );


    T findOrFail( Long id, LockModeType lockModeType );


    List< T > findAll();


    void remove( T entity );


    void persist( T entity );
}
