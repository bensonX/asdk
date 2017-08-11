package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.UChannelMaster;

/**
 * 渠道商缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:17
 */
public class UChannelMasterCache extends UCache<Integer,UChannelMaster> {
    private static UChannelMasterCache instance;

    private UChannelMasterCache() {

    }

    public static UChannelMasterCache getInstance() {
        if (instance == null) {
            instance = new UChannelMasterCache();
        }
        return instance;
    }
}
