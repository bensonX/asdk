package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UChargeChannel;
import org.springframework.stereotype.Repository;

/**
 * @author Lance Chow
 *         2016-09-07 10:01
 *        充值渠道商管理
 */
@Repository
public class UChargeChannelDao extends UHibernateTemplate<UChargeChannel,Integer> {
}
