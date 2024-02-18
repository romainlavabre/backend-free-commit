package com.free.commit.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.romainlavabre.database.StampManagedEntity;
import org.romainlavabre.exception.HttpNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
abstract public class AbstractRepository< T > {

    protected       Class< T >               classType;
    protected       JpaRepository< T, Long > jpaRepository;
    protected final EntityManager            entityManager;
    @Autowired
    private         StampManagedEntity       stampManagedEntity;


    public AbstractRepository( final EntityManager entityManager, final JpaRepository< T, Long > jpaRepository ) {
        this.entityManager = entityManager;
        this.jpaRepository = jpaRepository;
        this.classType     = this.getClassType();
    }


    public Optional< T > findById( final long id ) {
        return this.jpaRepository.findById( id );
    }


    public Optional< T > findById( final long id, final LockModeType lockModeType ) {
        return Optional.ofNullable(
                this.entityManager.find( this.classType, id, lockModeType )
        );
    }


    public T findOrFail( final Long id ) {
        if ( id == null ) {
            throw new HttpNotFoundException( this.buildEntityNameErrorMessage() + "_NOT_FOUND" );
        }

        final Optional< T > optionalT = this.jpaRepository.findById( id );

        if ( optionalT.isPresent() ) {
            return optionalT.get();
        }

        throw new HttpNotFoundException( this.buildEntityNameErrorMessage() + "_NOT_FOUND" );
    }


    public T findOrFail( final Long id, final LockModeType lockModeType ) {

        if ( id == null ) {
            throw new HttpNotFoundException( this.buildEntityNameErrorMessage() + "_NOT_FOUND" );
        }

        final Optional< T > optionalT = this.jpaRepository.findById( id );

        if ( optionalT.isPresent() ) {
            return optionalT.get();
        }

        throw new HttpNotFoundException( this.buildEntityNameErrorMessage() + "_NOT_FOUND" );
    }


    public List< T > findAll() {
        return this.jpaRepository.findAll();
    }


    public void remove( final T entity ) {
        this.stampManagedEntity.remove( entity );
    }


    public void persist( final T entity ) {
        this.stampManagedEntity.buffer( entity );
    }


    private String buildEntityNameErrorMessage() {
        final char[] letters = this.classType.getSimpleName().toCharArray();

        final StringBuilder message = new StringBuilder();

        for ( int i = 0; i < letters.length; i++ ) {
            final String letter = String.valueOf( letters[ i ] );

            if ( i > 0 && letter.matches( "[A-Z]" ) ) {
                message.append( "_" )
                        .append( letter.toUpperCase() );
                continue;
            }

            message.append( letter.toUpperCase() );
        }

        return message.toString();
    }


    abstract protected Class< T > getClassType();
}
