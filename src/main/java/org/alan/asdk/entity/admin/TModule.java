package org.alan.asdk.entity.admin;

import org.alan.asdk.cache.impl.manager.TModuleCache;
import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 模块
 *
 * @author Lance Chow
 * @create 2016-01-14 14:34
 */
@Entity
@Table(name = "umodule")
public class TModule implements Serializable {

    @Id
    private int id;
    /**
     * 模块名称
     */
    private String name;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 页面地址
     */
    private String url;
    /**
     * 图标
     */
    private String iconCls;
    /**
     * 最低要求权限
     */
    private Integer permission;
    /**
     * 是否为根节点
     */
    private boolean leaf;
    /**
     * 父级模块
     */
    private int pid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("id",this.id);
        j.put("name",this.name);
        j.put("text",this.name);
        j.put("url",this.url);
        j.put("namespace",this.namespace);
        j.put("iconCls",this.iconCls);
        j.put("icon",this.iconCls);
        j.put("permission",this.permission);
        j.put("leaf",this.leaf);
        j.put("pid",this.pid);
        TModule module = TModuleCache.getInstance().get(pid);
        String pName = "#父节点不存在#";
        if (module!=null){
            pName = module.name;
        }
        if (this.pid == -1){
            pName = "根节点";
        }
        j.put("pName", pName);
        return j;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
