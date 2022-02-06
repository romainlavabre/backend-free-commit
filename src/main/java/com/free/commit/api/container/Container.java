package com.free.commit.api.container;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 * {@see https://github.com/romainlavabre/spring-starter-container.git}
 */
public interface Container {

    /**
     * @param beanName Bean name
     * @return The requested bean
     */
    Object getInstance( String beanName );


    /**
     * @param type Bean class
     * @return The requested bean
     */
    < T > T getInstance( Class< T > type );
}
