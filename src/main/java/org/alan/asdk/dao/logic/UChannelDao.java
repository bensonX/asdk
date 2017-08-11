package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UChannel;
import org.springframework.stereotype.Repository;

/**
 * 渠道数据访问类
 */
@Repository("channelDao")
public class UChannelDao extends UHibernateTemplate<UChannel, Integer> {

    public void saveChannel(UChannel channel) {
        super.save(channel);
    }

    public UChannel queryChannel(int id) {

        return super.get(id);
    }
}
