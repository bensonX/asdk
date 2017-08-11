package org.alan.asdk.dao.manager;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.admin.TModule;
import org.springframework.stereotype.Repository;

/**
 * 模块管理DAO
 *
 * @author Lance Chow
 * @create 2016-01-14 15:08
 */
@Repository("moduleDao")
public class TModuleDao extends UHibernateTemplate<TModule,Integer> {
}
