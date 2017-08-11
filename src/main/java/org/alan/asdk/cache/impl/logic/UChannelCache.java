package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.UChannel;

/**
 * 渠道缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:23
 */
public class UChannelCache extends UCache<Integer,UChannel> {

    private static UChannelCache instance;

    private UChannelCache() {

    }

    public static UChannelCache getInstance() {
        if (instance == null) {
            instance = new UChannelCache();
        }
        return instance;
    }

}
