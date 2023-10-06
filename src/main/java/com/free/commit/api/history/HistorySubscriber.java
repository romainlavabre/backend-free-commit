package com.free.commit.api.history;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface HistorySubscriber {
    /**
     * @param object entity created
     */
    void create( Object object, History history );


    /**
     * @param object   entity updated
     * @param property property updated
     */
    void update( Object object, String property, History history );


    /**
     * @param object entity removed
     */
    void delete( Object object, History history );
}
