package org.alan.asdk.service;

import org.alan.asdk.cache.impl.logic.UChargeChannelCache;
import org.alan.asdk.common.Log;
import org.alan.asdk.dao.logic.UChargeChannelDao;
import org.alan.asdk.entity.UChargeChannel;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值渠道商服务层
 *
 * @author Lance Chow
 *         2016-09-07 10:02
 */
@Service
public class UChargeChannelManager {


    @Autowired
    private UChargeChannelDao dao;

    public List<UChargeChannel> getAll(){
        return UChargeChannelCache.getInstance().getAll();
    }

    public List<UChargeChannel> getAllByAdmin(TAdmin admin){

        if (admin.getPermission() == TAdmin.DEVELOPER){
            return getAll();
        }
        List<UChargeChannel> result = new ArrayList<>();
        for (UChargeChannel chargeChannel : getAll()) {
            if (chargeChannel.getAdminID() == admin.getId()){
                result.add(chargeChannel);
            }
        }
        return result;
    }

    public UChargeChannel get(int id){
        return UChargeChannelCache.getInstance().get(id);
    }

    public void save(UChargeChannel chargeChannel , TAdmin admin){
        Assert.notNull(admin);
        Assert.notNull(chargeChannel);
        //判断权限
        if (chargeChannel.getId() == 0){
            chargeChannel.setId(IDGenerator.getInstance().nextChargeChannelID());
        }
        if (admin.getPermission() > TAdmin.MANAGER){
            Log.i("用户["+admin.getFullName()+"]修改充值渠道商失败,权限不足!");
            return;
        }
        Log.i("用户["+admin.getFullName()+"]保存渠道成功:"+chargeChannel.toString());
        UChargeChannelCache.getInstance().save(chargeChannel);
        dao.save(chargeChannel);
    }

    public void remove(int id , TAdmin admin){
        Assert.notNull(admin);
        if (admin.getPermission() > TAdmin.MANAGER){
            Log.i("用户["+admin.getFullName()+"]删除充值渠道商失败,权限不足!");
            return;
        }
        UChargeChannelCache.getInstance().remove(id);
        dao.delete(id);
    }

}
