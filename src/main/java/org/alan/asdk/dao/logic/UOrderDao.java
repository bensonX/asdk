package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UOrder;
import org.springframework.stereotype.Repository;

/**
 * 订单数据访问类
 */
@Repository("orderDao")
public class UOrderDao extends UHibernateTemplate<UOrder, Long> {


}
