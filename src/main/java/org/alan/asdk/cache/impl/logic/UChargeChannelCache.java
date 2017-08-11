package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.UChargeChannel;

/**
 * 充值渠道商缓存
 *
 * @author Lance Chow
 *         2016-09-07 10:05
 */
public class UChargeChannelCache extends UCache<Integer,UChargeChannel> {
    private static UChargeChannelCache instance;

    private UChargeChannelCache() {

    }

    public static UChargeChannelCache getInstance() {
        if (instance == null) {
            instance = new UChargeChannelCache();
        }
        return instance;
    }

}
