package org.alan.asdk.entity;
import javax.persistence.*;

/**
 * Created by Administrator on 2015-12-02.
 */
@Entity
@Table(name = "t_user")
public class UAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int appID;

    private int channelID;

    private String username;

    private String psw;

    private String realPsw;

    private String email;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealPsw() {
        return realPsw;
    }

    public void setRealPsw(String realPsw) {
        this.realPsw = realPsw;
    }
}
