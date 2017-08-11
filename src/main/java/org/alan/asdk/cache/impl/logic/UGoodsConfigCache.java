package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.UGoodsConfig;

/**
 * 商品配置缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:30
 */
public class UGoodsConfigCache extends UCache<Integer,UGoodsConfig> {

    private static UGoodsConfigCache instance;

    private UGoodsConfigCache() {

    }

    public static UGoodsConfigCache getInstance() {
        if (instance == null) {
            instance = new UGoodsConfigCache();
        }
        return instance;
    }

}
