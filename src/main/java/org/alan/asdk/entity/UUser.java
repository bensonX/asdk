package org.alan.asdk.entity;

import org.alan.asdk.cache.impl.logic.UChannelCache;
import org.alan.asdk.cache.impl.logic.UGameCache;
import org.alan.asdk.utils.TimeFormater;
import net.sf.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户数据对象
 */

@Entity
@Table(name = "uuser")
public class UUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//以数据库组件自动增长
    private int id;
    private int appID;
    private int channelID;
    private String name;
    private String channelUserID;
    private String channelUserName;
    private String channelUserNick;
    private Date createTime;
    private Date lastLoginTime;
    private String token;
    private String extension;

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("appID", appID);
        UGame game = getGame();
        json.put("appName", game == null ? "" : game.getName());
        json.put("channelID", channelID);
        UChannel channel = getChannel();
        json.put("channelName", channel == null ? "" : channel.getMaster().getMasterName());
        json.put("name", name);
        json.put("channelUserID", channelUserID);
        json.put("channelUserName", channelUserName);
        json.put("channelUserNick", channelUserNick);
        json.put("createTime", TimeFormater.format_default(createTime));
        json.put("lastLoginTime", TimeFormater.format_default(lastLoginTime));
        json.put("extension",this.extension);
        return json;
    }

    public UChannel getChannel() {
        return UChannelCache.getInstance().get(this.channelID);
    }

    public UGame getGame() {
        return UGameCache.getInstance().get(this.appID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelUserID() {
        return channelUserID;
    }

    public void setChannelUserID(String channelUserID) {
        this.channelUserID = channelUserID;
    }

    public String getChannelUserName() {
        return channelUserName;
    }

    public void setChannelUserName(String channelUserName) {
        this.channelUserName = channelUserName;
    }

    public String getChannelUserNick() {
        return channelUserNick;
    }

    public void setChannelUserNick(String channelUserNick) {
        this.channelUserNick = channelUserNick;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
