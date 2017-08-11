package org.alan.asdk.cache.impl.manager;

import org.alan.asdk.cache.UCache;
import org.alan.asdk.entity.admin.TAdmin;

/**
 * 管理员缓存
 *
 * @author Lance Chow
 *         2016-09-06 16:49
 */
public class TAdminCache extends UCache<Integer,TAdmin> {

    private static TAdminCache instance;

    private TAdminCache() {

    }

    public static TAdminCache getInstance() {
        if (instance == null) {
            instance = new TAdminCache();
        }
        return instance;
    }
}
