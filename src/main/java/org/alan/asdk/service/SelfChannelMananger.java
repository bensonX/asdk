package org.alan.asdk.service;

import org.alan.asdk.cache.impl.logic.SelfChannelCache;
import org.alan.asdk.dao.logic.SelfChannelDao;
import org.alan.asdk.entity.SelfChannel;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 自渠道配置服务层
 *
 * @author Lance Chow
 *         2016-09-12 11:23
 */
@Service
public class SelfChannelMananger {


    @Autowired
    private SelfChannelDao dao;
    @Autowired
    private UChannelManager channelManager;



    public SelfChannel get(int id){
        return  SelfChannelCache.getInstance().get(id);
    }

    public List<SelfChannel> getAll(){
        return  SelfChannelCache.getInstance().getAll();
    }

    public List<SelfChannel> getByAdmin(TAdmin admin){
        List<UChannel> channels = channelManager.getByAdmin(admin);
        List<SelfChannel> selfChannels = getAll();
        List<SelfChannel> rst = new ArrayList<>();
        for (UChannel channel : channels) {
            for (SelfChannel selfChannel : selfChannels) {
                if (channel.getChannelID() == selfChannel.getChannelID()){
                    rst.add(selfChannel);
                }
            }
        }
        return rst;
    }

    public void save(SelfChannel channel){
        if (channel.getConfigID() == 0 ){
            channel.setConfigID(IDGenerator.getInstance().nextSelfChannelID());
        }
        SelfChannelCache.getInstance().save(channel);
        dao.save(channel);
    }

    public void save(SelfChannel channel , TAdmin tAdmin){
        Assert.notNull(tAdmin);
        if (tAdmin.getPermission() > TAdmin.MANAGER){
            return;
        }
        save(channel);
    }

    public void remove(int configID){
        SelfChannelCache.getInstance().remove(configID);
        dao.delete(configID);
    }

    public void remove(int configID , TAdmin admin){
        if (admin.getPermission() <= TAdmin.MANAGER){
            return;
        }
        remove(configID);
    }


}
