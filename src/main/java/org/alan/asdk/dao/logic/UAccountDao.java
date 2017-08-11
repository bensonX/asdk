package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UAccount;
import org.springframework.stereotype.Repository;

/**
 * 自渠道用户
 */
@Repository("accountDao")
public class UAccountDao extends UHibernateTemplate<UAccount,Integer> {
}
