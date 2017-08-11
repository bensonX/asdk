package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UGoodsConfig;
import org.springframework.stereotype.Repository;

/**
 * 商品配置
 *
 * @author Lance Chow
 * @create 2015-12-28 10:24
 */
@Repository("goodsConfigDao")
public class UGoodsConfigDao extends UHibernateTemplate<UGoodsConfig , Integer> {
}
