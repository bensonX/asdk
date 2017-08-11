package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.SelfChannel;
import org.springframework.stereotype.Repository;

/**
 * 自渠道配置DAO
 *
 * @author Lance Chow
 *         2016-09-12 11:25
 */
@Repository
public class SelfChannelDao extends UHibernateTemplate<SelfChannel,Integer> {
}
