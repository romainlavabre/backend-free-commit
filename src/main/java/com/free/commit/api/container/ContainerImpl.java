package com.free.commit.api.container;


import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ContainerImpl implements Container, ApplicationContextAware {

    protected ApplicationContext applicationContext;


    @Override
    public void setApplicationContext( final ApplicationContext applicationContext ) {
        this.applicationContext = applicationContext;
    }


    @Override
    public Object getInstance( final String classname ) {

        assert classname != null && !classname.isBlank() : "classname variable should not be null or blank";

        if ( this.applicationContext.containsBean( classname ) ) {
            return this.applicationContext.getBean( classname );
        }

        final String[] res = classname.split( "\\." );

        final String qualifier = Character.toLowerCase( res[ res.length - 1 ].charAt( 0 ) ) + res[ res.length - 1 ].substring( 1 );

        try {
            return BeanFactoryAnnotationUtils
                    .qualifiedBeanOfType(
                            this.applicationContext.getAutowireCapableBeanFactory(),
                            Class.forName( classname ),
                            qualifier
                    );
        } catch ( final ClassNotFoundException e ) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    public < T > T getInstance( final Class< T > type ) {
        return this.applicationContext.getBean( type );
    }
}
