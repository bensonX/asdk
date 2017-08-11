package org.alan.asdk.service;

import org.alan.asdk.cache.impl.logic.UGoodsCache;
import org.alan.asdk.dao.logic.UGoodsDao;
import org.alan.asdk.entity.UGoods;
import org.alan.asdk.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("uGoodsManager")
public class UGoodsManager {

    @Autowired
    private UGoodsDao dao;

    public void saveGoods(UGoods uGoods){
        if(uGoods ==null){
            return;
        }
        if (uGoods.getGoodsID()==null || uGoods.getGoodsID()<=0){
            uGoods.setGoodsID(IDGenerator.getInstance().nextGoodsID());
        }
        UGoodsCache.getInstance().save(uGoods);
        dao.save(uGoods);
    }

    public void removeGoods(int goodsId){
        dao.delete(goodsId);
        UGoodsCache.getInstance().remove(goodsId);
    }


    /**
     * 通过商品配置查找商品列表
     * @param configID 商品配置ID
     * @return 返回商品对象列表
     */
    public List<UGoods> queryGoodsPage(int configID) {
        List<UGoods> goodsList = UGoodsCache.getInstance().getAll();
        List<UGoods> resultList = new ArrayList<>();
        for (UGoods goods : goodsList){
            if (goods.getGoodsConfigID()==configID){
                resultList.add(goods);
            }
        }
        return resultList;
    }

    /**
     * 通过编号查找商品
     * @param configID goodsConfigID
     * @param productID 商品编号
     * @return 商品对象
     */
    public UGoods getGoodsByProductID(int configID, String productID) {
        if(configID==0||productID==null){
            return null;
        }
        List<UGoods> goodsList = queryGoodsPage(configID);
        for (UGoods goodsConfig : goodsList){
            if (goodsConfig.getProductID().equals(productID)){
                return goodsConfig;
            }
        }
        return null;
    }

    /**
     * 通过渠道对应的商品ID查询商品
     * @return 商品对象
     */
    public UGoods getGoodsBySdkGoodsID(int configID , String sdkGoodsID){
        if (configID==0 || StringUtils.isEmpty(sdkGoodsID)){
            return null;
        }
        List<UGoods> goodsList = queryGoodsPage(configID);
        for (UGoods uGoods : goodsList) {
            if (uGoods.getSdkGoodsID().equals(sdkGoodsID)){
                return  uGoods;
            }
        }
        return null;
    }
}
