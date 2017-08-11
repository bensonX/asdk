package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.UGoods;

/**
 * 商品缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:27
 */
public class UGoodsCache extends UCache<Integer,UGoods> {

    private static UGoodsCache instance;

    private UGoodsCache() {

    }

    public static UGoodsCache getInstance() {
        if (instance == null) {
            instance = new UGoodsCache();
        }
        return instance;
    }

}
