package org.alan.asdk.entity.admin;

import org.alan.asdk.cache.impl.manager.TAdminCache;
import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "uadmin")
public class TAdmin {

    @Id
    private Integer id;
    private String username;
    private String password;
    private String fullName;
    /**
     * 父级用户ID
     */
    private Integer pAdminID;
    private int permission;
    /**
     * 开发者权限
     */
    public static final int DEVELOPER  = 0;
    /**
     * 管理员权限
     */
    public static final int MANAGER = 1;
    /**
     * 操作员权限
     */
    public static final int OPERATOR = 2;
    /**
     * 客服权限
     */
    public static final int SERVER = 3;

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();

        json.put("id",this.id);
        json.put("username",this.username);
        json.put("password",this.password);
        json.put("pAdminID",this.pAdminID);
        json.put("pAdminName", getPUser()==null ? "顶级用户" : getPUser().getFullName() );
        json.put("permission",this.permission);
        json.put("fullName",this.fullName);

        return json;
    }

    public TAdmin getPUser(){
        return TAdminCache.getInstance().get(this.pAdminID);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }


    public Integer getpAdminID() {
        return pAdminID;
    }

    public void setpAdminID(Integer pAdminID) {
        this.pAdminID = pAdminID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
