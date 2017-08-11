package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.UGame;

/**
 * 游戏缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:08
 */
public class UGameCache extends UCache<Integer , UGame> {
    private static UGameCache instance;

    private UGameCache() {

    }

    public static UGameCache getInstance() {
        if (instance == null) {
            instance = new UGameCache();
        }
        return instance;
    }
}
