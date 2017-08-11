package org.alan.asdk.entity;

import org.alan.asdk.cache.impl.logic.UChannelMasterCache;
import org.alan.asdk.cache.impl.logic.UGameCache;
import org.alan.asdk.cache.impl.logic.UGoodsCache;
import org.alan.asdk.cache.impl.logic.UGoodsConfigCache;
import org.alan.asdk.cache.impl.manager.TAdminCache;
import org.alan.asdk.entity.admin.TAdmin;
import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * 渠道对象(每个游戏对某个渠道)
 */

@Entity
@Table(name = "uchannel")
public class UChannel {

    @Id
    private Integer channelID;          //渠道ID 和客户端一致
    private int appID;                  //游戏ID
    private int masterID;               //渠道商ID
    private int goodsConfigID;          //商品配置ID
    private String channelName;
    private String cpID;                //渠道分配给游戏的cpID
    private String cpAppID;             //渠道分配给游戏的appID
    private String cpAppKey;            //渠道分配给游戏的AppKey
    private String cpAppSecret;         //渠道分配给游戏的AppSecret

    private String cpPayID;             //渠道分配给游戏的支付ID
    private String cpPayKey;            //渠道分配给游戏的支付公钥
    private String cpPayPriKey;         //渠道分配给游戏的支付私钥

    private String cpConfig;            //部分渠道可能有特殊配置信息，设置在该字段中

    private int createAdminID;
    /**
     * 通过渠道获取渠道商对象
     * @return 渠道商对象
     */
    public UChannelMaster getMaster() {
        return UChannelMasterCache.getInstance().get(masterID);
    }

    public UGame getGame() {
        return UGameCache.getInstance().get(this.appID);
    }

    public TAdmin getCreateAdmin(){
        return TAdminCache.getInstance().get(createAdminID);
    }

    public List<UGoods> getGoodsList(){
        List<UGoods> goodsList = UGoodsCache.getInstance().getAll();
        List<UGoods> resault = new ArrayList<>();
        for (UGoods goods : goodsList) {
            if (goods.getGoodsConfigID() == this.goodsConfigID){
                resault.add(goods);
            }
        }
        return resault;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("channelID", channelID);
        json.put("appID", appID);
        UGame game = getGame();
        json.put("appName", game == null ? "" : game.getName());
        json.put("masterID", masterID);
        UChannelMaster master = getMaster();
        json.put("masterName", master == null ? "" : master.getMasterName());
        json.put("channelName", this.channelName);
        json.put("goodsConfigID",goodsConfigID);
        UGoodsConfig config = getUGoodsConfig();
        if (config!=null)
        json.put("goodsConfigName",config.getName());
        TAdmin admin = getCreateAdmin();
        json.put("createAdminID",this.createAdminID);
        json.put("createAdminName",admin==null? "完全公开" : admin.getFullName());
        json.put("cpID", cpID);
        json.put("cpAppID", cpAppID);
        json.put("cpAppKey", cpAppKey);
        json.put("cpAppSecret", cpAppSecret);
        json.put("cpPayID", cpPayID);
        json.put("cpPayKey", cpPayKey);
        json.put("cpPayPriKey", cpPayPriKey);
        json.put("cpConfig", cpConfig);
        return json;
    }

    private UGoodsConfig getUGoodsConfig() {
        return UGoodsConfigCache.getInstance().get(goodsConfigID);
    }

    public Integer getChannelID() {
        return channelID;
    }

    public void setChannelID(Integer channelID) {
        this.channelID = channelID;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public int getMasterID() {
        return masterID;
    }

    public void setMasterID(int masterID) {
        this.masterID = masterID;
    }

    public String getCpID() {
        return cpID;
    }

    public void setCpID(String cpID) {
        this.cpID = cpID;
    }

    public String getCpAppID() {
        return cpAppID;
    }

    public void setCpAppID(String cpAppID) {
        this.cpAppID = cpAppID;
    }

    public String getCpAppKey() {
        return cpAppKey;
    }

    public void setCpAppKey(String cpAppKey) {
        this.cpAppKey = cpAppKey;
    }

    public String getCpPayKey() {
        return cpPayKey;
    }

    public void setCpPayKey(String cpPayKey) {
        this.cpPayKey = cpPayKey;
    }

    public String getCpAppSecret() {
        return cpAppSecret;
    }

    public void setCpAppSecret(String cpAppSecret) {
        this.cpAppSecret = cpAppSecret;
    }

    public String getCpConfig() {
        return cpConfig;
    }

    public void setCpConfig(String cpConfig) {
        this.cpConfig = cpConfig;
    }

    public String getCpPayID() {
        return cpPayID;
    }

    public void setCpPayID(String cpPayID) {
        this.cpPayID = cpPayID;
    }

    public String getCpPayPriKey() {
        return cpPayPriKey;
    }

    public void setCpPayPriKey(String cpPayPriKey) {
        this.cpPayPriKey = cpPayPriKey;
    }


    public int getCreateAdminID() {
        return createAdminID;
    }

    public void setCreateAdminID(int createAdminID) {
        this.createAdminID = createAdminID;
    }

    public int getGoodsConfigID() {
        return goodsConfigID;
    }

    public void setGoodsConfigID(int goodsConfigID) {
        this.goodsConfigID = goodsConfigID;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
