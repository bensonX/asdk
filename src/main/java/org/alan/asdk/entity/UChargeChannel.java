package org.alan.asdk.entity;

import org.alan.asdk.cache.impl.manager.TAdminCache;
import org.alan.asdk.entity.admin.TAdmin;
import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 充值渠道商
 *
 * @author Lance Chow
 *         2016-09-06 14:52
 */
@Entity
@Table(name = "uchargechannel")
public class UChargeChannel {
    @Id
    private int id;
    //充值渠道名称
    private String chargeChannelName;
    //appID
    private String appID;
    //appKey
    private String appKey;
    //appSecret
    private String appSecret;
    //如果前三个字段不能满足 该字段为填充字段 格式为json字符串
    private String config;
    //充值渠道的图标地址
    private String iconUrl;
    //充值渠道类路径
    private String scriptClass;
    //充值链接地址
    private String url;
    //所有者
    private int adminID;

    public JSONObject toJSON(){
        JSONObject js = new JSONObject();
        js.put("id",this.id);
        js.put("chargeChannelName",this.chargeChannelName);
        js.put("appID",this.appID);
        js.put("appKey",this.appKey);
        js.put("appSecret",this.appSecret);
        js.put("config",this.config);
        js.put("iconUrl",this.iconUrl);
        js.put("scriptClass",this.scriptClass);
        js.put("url",this.url);
        js.put("adminID",this.adminID);
        TAdmin admin = TAdminCache.getInstance().get(this.adminID);
        if (admin!=null){
            js.put("adminName", admin.getFullName());
        }else{
            js.put("adminName",this.adminID +" 用户已经被删除");
        }
        return js;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public String getChargeChannelName() {
        return chargeChannelName;
    }

    public void setChargeChannelName(String chargeChannelName) {
        this.chargeChannelName = chargeChannelName;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getScriptClass() {
        return scriptClass;
    }

    public void setScriptClass(String scriptClass) {
        this.scriptClass = scriptClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }
}
