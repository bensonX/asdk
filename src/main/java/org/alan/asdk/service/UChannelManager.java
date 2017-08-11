package org.alan.asdk.service;

import org.alan.asdk.cache.impl.logic.UChannelCache;
import org.alan.asdk.dao.logic.UChannelDao;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.admin.UAdminManager;
import org.alan.asdk.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 */
@Service("channelManager")
public class UChannelManager {

    @Autowired
    private UChannelDao channelDao;
    @Autowired
    private UAdminManager adminManager;

    public UChannel generateChannel(int appID, int masterID, String CpID, String CpAppID, String CpAppKey, String CpAppSecret, String CpPayKey) {

        return generateChannel(appID, masterID, CpID, CpAppID, CpAppKey, CpAppSecret, CpPayKey, "", "");
    }

    public UChannel generateChannel(int appID, int masterID, String CpID, String CpAppID, String CpAppKey, String CpAppSecret, String CpPayKey, String CpPayPriKey, String CpPayID) {
        UChannel channel = new UChannel();
        channel.setAppID(appID);
        channel.setMasterID(masterID);

        channel.setCpID(CpID);
        channel.setCpAppID(CpAppID);
        channel.setCpAppKey(CpAppKey);
        channel.setCpAppSecret(CpAppSecret);
        channel.setCpPayKey(CpPayKey);
        channel.setCpPayPriKey(CpPayPriKey);
        channel.setCpPayID(CpPayID);


        saveChannel(channel);

        return channel;
    }

    public int getChannelCount(TAdmin tAdmin) {

        return getByAdmin(tAdmin).size();
    }

    //分页查找
    public List<UChannel> queryPage(int currPage, int num , TAdmin tAdmin) {

        List<UChannel> channels = getByAdmin(tAdmin);

        Collections.sort(channels, new Comparator<UChannel>() {
            @Override
            public int compare(UChannel o1, UChannel o2) {
                return o1.getChannelID() - o2.getChannelID();
            }
        });

        int fromIndex = (currPage - 1) * num;

        if (fromIndex >= channels.size()) {

            return null;
        }

        int endIndex = Math.min(fromIndex + num, channels.size());

        return channels.subList(fromIndex, endIndex);
    }

    //添加或者修改channel
    public void saveChannel(UChannel channel) {

        if (channel.getChannelID() == null || channel.getChannelID() <= 0) {
            channel.setChannelID(IDGenerator.getInstance().nextChannelID());
        }

        UChannelCache.getInstance().save(channel);
        channelDao.save(channel);
    }

    public UChannel queryChannel(int id) {

        return UChannelCache.getInstance().get(id);
    }

    /**
     * 通过参数找到channel
     * @param channel 满足任意值既可查询出来
     * @return UChannel对象
     */
    public UChannel getChannelByParams(UChannel channel){
        List<UChannel> channels = UChannelCache.getInstance().getAll();
        for (UChannel uChannel : channels) {
            if(uChannel.getCpID()!=null&&uChannel.getCpID().equals(channel.getCpID())){
                return uChannel;
            }
            if(uChannel.getCpAppID()!=null&&uChannel.getCpAppID().equals(channel.getCpAppID())){
                return uChannel;
            }
            if(uChannel.getCpAppKey()!=null&&uChannel.getCpAppKey().equals(channel.getCpAppKey())){
                return uChannel;
            }
            if(uChannel.getCpAppSecret()!=null&&uChannel.getCpAppSecret().equals(channel.getCpAppSecret())){
                return uChannel;
            }
            if(uChannel.getCpPayID()!=null&&uChannel.getCpPayID().equals(channel.getCpPayID())){
                return uChannel;
            }
            if(uChannel.getCpPayKey()!=null&&uChannel.getCpPayKey().equals(channel.getCpPayKey())){
                return uChannel;
            }
            if(uChannel.getCpPayPriKey()!=null&&uChannel.getCpPayPriKey().equals(channel.getCpPayPriKey())){
                return uChannel;
            }
            if(uChannel.getCpConfig()!=null&&uChannel.getCpConfig().equals(channel.getCpConfig())){
                return uChannel;
            }
        }
        return null;
    }

    public void deleteChannel(UChannel channel) {
        if (channel == null) {
            return;
        }

        UChannelCache.getInstance().remove(channel.getChannelID());
        channelDao.delete(channel.getChannelID());
    }

    /**
     * 通过渠道商获取属于该渠道商的所有渠道
     * @param sdkName 渠道商名称
     * @return channelList
     */
    public List<UChannel> getChannelBySDKName(String sdkName){
        List<UChannel> allChannels = UChannelCache.getInstance().getAll();
        List<UChannel> resault = new ArrayList<>();
        for (UChannel channel : allChannels) {
            if (Objects.equals(channel.getMaster().getSdkName(), sdkName)){
                resault.add(channel);
            }
        }
        return  resault;
    }

    /**
     * 通过商品配置查找商品
     * @param configID 商品配置ID
     * @return channelList
     */
    public List<UChannel> getChannelByGoodsConfigID(int configID){
        List<UChannel> allChannels = UChannelCache.getInstance().getAll();
        List<UChannel> resault = new ArrayList<>();
        for (UChannel channel : allChannels) {
            if (channel.getGoodsConfigID() == configID){
                resault.add(channel);
            }
        }
        return  resault;
    }

    public List<UChannel> getByAdmin(TAdmin tAdmin){
        List<UChannel> masters = UChannelCache.getInstance().getAll();
        if (tAdmin !=null && tAdmin.getPermission()!=0){
            List<UChannel> list = new ArrayList<UChannel>();
            for (UChannel channel : masters){
                if (channel.getCreateAdminID() == -1){
                    list.add(channel);
                    continue;
                }
                if (tAdmin.getId().equals(channel.getCreateAdminID())){
                    list.add(channel);
                    continue;
                }
                //创建用户是父级用户可见
                if (adminManager.isPUser(channel.getCreateAdminID(), tAdmin.getId())){
                    list.add(channel);
                    continue;
                }
                //创建用户是子级用户可见
                if (adminManager.isCUser(channel.getCreateAdminID(), tAdmin.getId())){
                    list.add(channel);
                }
            }
            masters = list;
        }
        return masters;
    }


}
