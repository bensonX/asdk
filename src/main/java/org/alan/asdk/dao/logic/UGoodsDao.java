package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UGoods;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015-11-09.
 */
@Repository("uGoodsDao")
public class UGoodsDao extends UHibernateTemplate<UGoods, Integer> {

    public void saveUGoodsDao(UGoods config){
        super.save(config);
    }

    public UGoods queryGoodsConfig(int id){
        return super.get(id);
    }
}
