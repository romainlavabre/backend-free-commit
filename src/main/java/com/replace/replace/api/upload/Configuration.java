package com.replace.replace.api.upload;

import com.replace.replace.api.container.Container;
import com.replace.replace.api.upload.Move.MoveRule;
import com.replace.replace.api.upload.annotation.*;
import com.replace.replace.api.upload.duplication.DuplicationRule;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Configuration {

    protected Container                            container;
    protected Map< String, Map< String, Object > > repository = new HashMap<>();


    public Configuration( Container container ) throws IllegalAccessException {
        this.container = container;
        this.load();
    }

    public boolean hasMaxSize( String config ) {
        return ( int ) this.repository
                .get( config )
                .get( Size.class.getName() ) > 0;
    }

    public int getSize( String config ) {
        return ( int ) this.repository
                .get( config )
                .get( Size.class.getName() );
    }

    public MoveRule getMoveRule( String config ) {
        return ( MoveRule ) this.repository
                .get( config )
                .get( Move.class.getName() );
    }

    public DuplicationRule getDuplicationRule( String config ) {
        return ( DuplicationRule ) this.repository
                .get( config )
                .get( Duplication.class.getName() );
    }

    public boolean isAcceptAllType( String config ) {
        return this.repository
                .get( config )
                .get( AcceptType.class.getName() ) == null;

    }

    public String[] getAcceptType( String config ) {
        return ( String[] ) this.repository
                .get( config )
                .get( AcceptType.class.getName() );
    }

    public boolean isTransactionSynchronized( String config ) {
        return ( boolean ) this.repository
                .get( config )
                .get( TransactionSynchronized.class.getName() );
    }

    protected void load() throws IllegalAccessException {
        for ( Field field : UploadHandler.class.getDeclaredFields() ) {
            Move                    move                    = field.getAnnotation( Move.class );
            Duplication             duplication             = field.getAnnotation( Duplication.class );
            AcceptType              acceptType              = field.getAnnotation( AcceptType.class );
            Size                    size                    = field.getAnnotation( Size.class );
            TransactionSynchronized transactionSynchronized = field.getAnnotation( TransactionSynchronized.class );
            String                  name                    = ( String ) field.get( null );

            this.repository.put( name, new HashMap<>() );

            this.repository.get( name )
                           .put(
                                   Move.class.getName(),
                                   move != null
                                           ? this.container.getInstance( move.moveRule().getName() )
                                           : null
                           );

            this.repository.get( name )
                           .put(
                                   Duplication.class.getName(),
                                   duplication != null
                                           ? this.container.getInstance( duplication.duplicationRule().getName() )
                                           : null
                           );

            this.repository.get( name )
                           .put(
                                   AcceptType.class.getName(),
                                   acceptType == null || acceptType.all()
                                           ? null
                                           : acceptType.types()
                           );

            this.repository.get( name )
                           .put(
                                   Size.class.getName(),
                                   size == null || size.size() == 0
                                           ? 0
                                           : size.size()
                           );

            this.repository.get( name )
                           .put(
                                   TransactionSynchronized.class.getName(),
                                   transactionSynchronized != null && transactionSynchronized.value()
                           );
        }
    }
}
