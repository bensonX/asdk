package org.alan.asdk.entity;

import net.sf.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品类型
 *
 * @author Lance Chow
 * @create 2015-12-28 10:10
 */
@Entity
@Table(name = "ugoods_config")
public class UGoodsConfig {

    /**
     * id
     */
    @Id
    private int id;
    /**
     * 名称
     */
    private String name;
    /**
     * 创建者ID
     */
    private int createAdminID;

    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        jo.put("id",this.id);
        jo.put("name" , this.name);
        jo.put("createAdminID",this.createAdminID);
        return jo;
    }


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

    public int getCreateAdminID() {
        return createAdminID;
    }

    public void setCreateAdminID(int createAdminID) {
        this.createAdminID = createAdminID;
    }


}
