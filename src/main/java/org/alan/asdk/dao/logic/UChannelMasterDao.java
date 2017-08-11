package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UChannelMaster;
import org.springframework.stereotype.Repository;

/**
 * 渠道商数据访问类
 */
@Repository("channelMasterDao")
public class UChannelMasterDao extends UHibernateTemplate<UChannelMaster, Integer> {

    public void saveChannelMaster(UChannelMaster master) {
        super.save(master);
    }

    public UChannelMaster queryChannelMaster(int channelID) {

        return super.get(channelID);
    }

}
