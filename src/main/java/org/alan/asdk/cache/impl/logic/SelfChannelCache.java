package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.SelfChannel;

/**
 * 自渠道配置缓存
 *
 * @author Lance Chow
 *         2016-09-12 11:26
 */
public class SelfChannelCache extends UCache<Integer,SelfChannel> {
    private static SelfChannelCache instance;

    private SelfChannelCache() {

    }

    public static SelfChannelCache getInstance() {
        if (instance == null) {
            instance = new SelfChannelCache();
        }
        return instance;
    }
}
