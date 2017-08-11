package org.alan.asdk.dao.manager;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.admin.TAdmin;
import org.springframework.stereotype.Repository;

/**
 * Admin数据访问
 * Created by ant on 2015/8/29.
 */
@Repository("adminDao")
public class TAdminDao extends UHibernateTemplate<TAdmin, Integer> {
}
