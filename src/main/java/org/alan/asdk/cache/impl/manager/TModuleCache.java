package org.alan.asdk.cache.impl.manager;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.admin.TModule;

/**
 * 模块缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:41
 */
public class TModuleCache extends UCache<Integer , TModule> {
    private static TModuleCache instance;

    private TModuleCache() {

    }

    public static TModuleCache getInstance() {
        if (instance == null) {
            instance = new TModuleCache();
        }
        return instance;
    }
}
