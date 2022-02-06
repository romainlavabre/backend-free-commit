package com.free.commit.api.storage.data;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface LockType {
    Integer PESSIMISTIC_READ  = 4;
    Integer PESSIMISTIC_WRITE = 5;
}
