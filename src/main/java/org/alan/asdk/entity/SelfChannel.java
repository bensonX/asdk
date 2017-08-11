package org.alan.asdk.entity;

import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自渠道配置
 *
 * @author Lance Chow
 *         2016-09-07 16:46
 */
@Entity
@Table(name = "self_channel_config")
public class SelfChannel {
    @Id
    private int configID;

    private int channelID;

    private int chargeChannelConfig;

    private String msgClass;

    private String language;

    private boolean openLogin;

    private boolean openRegister;

    public int getConfigID() {
        return configID;
    }

    public void setConfigID(int configID) {
        this.configID = configID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public int getChargeChannelConfig() {
        return chargeChannelConfig;
    }

    public void setChargeChannelConfig(int chargeChannelConfig) {
        this.chargeChannelConfig = chargeChannelConfig;
    }

    public String getMsgClass() {
        return msgClass;
    }

    public void setMsgClass(String msgClass) {
        this.msgClass = msgClass;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isOpenLogin() {
        return openLogin;
    }

    public void setOpenLogin(boolean openLogin) {
        this.openLogin = openLogin;
    }

    public boolean isOpenRegister() {
        return openRegister;
    }

    public void setOpenRegister(boolean openRegister) {
        this.openRegister = openRegister;
    }

    public JSONObject toJSON() {
        JSONObject js = new JSONObject();
        js.put("configID",this.configID);
        js.put("channelID",this.channelID);
        js.put("chargeChannelConfig",this.chargeChannelConfig);
        js.put("msgClass",this.msgClass);
        js.put("language",this.language);
        js.put("openLogin",this.openLogin);
        js.put("openRegister",this.openRegister);
        return js;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
