package org.alan.asdk.service;

import org.alan.asdk.cache.impl.logic.UGoodsConfigCache;
import org.alan.asdk.dao.logic.UGoodsConfigDao;
import org.alan.asdk.entity.UGoodsConfig;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.service.admin.UAdminManager;
import org.alan.asdk.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 商品配置管理器
 *
 * @author Lance Chow
 * @create 2015-12-28 11:28
 */
@Service("uGoodsConfigManager")
public class UGoodsConfigManager {
    @Autowired
    private UGoodsConfigDao dao;
    @Autowired
    private UAdminManager adminManager;

    public List<UGoodsConfig> queryConfigPage(int currPage, int num , TAdmin tAdmin){
        if (currPage==0){
            return getConfigsByAdmin(tAdmin);
        }
        Collections.sort(getConfigsByAdmin(tAdmin), new Comparator<UGoodsConfig>() {
            @Override
            public int compare(UGoodsConfig o1, UGoodsConfig o2) {
                return o1.getId() - o2.getId();
            }
        });
        int fromIndex = (currPage - 1) * num;

        if (fromIndex >= getConfigsByAdmin(tAdmin).size()) {
            return null;
        }
        int endIndex = Math.min(fromIndex + num, getConfigsByAdmin(tAdmin).size());

        return getConfigsByAdmin(tAdmin).subList(fromIndex, endIndex);

    }
    public int getConfigCount(TAdmin tAdmin) {
        return getConfigsByAdmin(tAdmin).size();
    }

    public List<UGoodsConfig> queryAllConfig() {
        return UGoodsConfigCache.getInstance().getAll();
    }

    private List<UGoodsConfig> getConfigsByAdmin(TAdmin tAdmin){
        List<UGoodsConfig> masters = UGoodsConfigCache.getInstance().getAll();
        if (tAdmin !=null&& tAdmin.getPermission()!=0){
            List<UGoodsConfig> list = new ArrayList<>();
            for (UGoodsConfig configs : masters){
                if (configs.getCreateAdminID() == -1){
                    list.add(configs);
                    continue;
                }
                if (tAdmin.getId().equals(configs.getCreateAdminID())){
                    list.add(configs);
                    continue;
                }
                //创建用户是父级用户可见
                if (adminManager.isPUser(configs.getCreateAdminID(), tAdmin.getId())){
                    list.add(configs);
                    continue;
                }
                //创建用户是子级用户可见
                if (adminManager.isCUser(configs.getCreateAdminID(), tAdmin.getId())){
                    list.add(configs);
                }
            }
            masters = list;
        }
        return masters;
    }

    public void saveConfig(UGoodsConfig config){
        if(config ==null){
            return;
        }
        if (config.getId()<=0){
            config.setId(IDGenerator.getInstance().nextGoodsConfigID());
        }
        UGoodsConfigCache.getInstance().save(config);
        dao.save(config);
    }

    public void removeConfig(int currConfigID) {
        dao.delete(currConfigID);
        UGoodsConfigCache.getInstance().remove(currConfigID);
    }
}
