package org.alan.asdk.entity;

import net.sf.json.JSONObject;

import javax.persistence.*;

/**
 * Created by Chow on 2015-11-06.
 */

@Entity
@Table(name = "ugoods")
public class UGoods {
    @Id
    private Integer goodsID;

    private String goodsName;
    /**
     * 商品编号
     */
    private String productID;

    private int money;

    private String moneyUnit;

    private int diamond;

    private String sdkGoodsID;

    /**
     * 隶属于哪个商品配置ID的
     */
    private int goodsConfigID;

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("goodsID", goodsID);
        json.put("goodsName", goodsName);
        json.put("productID", productID);
        json.put("money", money);
        json.put("moneyUnit", moneyUnit);
        json.put("sdkGoodsID", sdkGoodsID);
        json.put("goodsConfigID", goodsConfigID);
        json.put("diamond", diamond);
        return json;
    }

    /** getter and setter**/
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(Integer goodsID) {
        this.goodsID = goodsID;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }

    public String getSdkGoodsID() {
        return sdkGoodsID;
    }

    public void setSdkGoodsID(String sdkGoodsID) {
        this.sdkGoodsID = sdkGoodsID;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getGoodsConfigID() {
        return goodsConfigID;
    }

    public void setGoodsConfigID(int goodsConfigID) {
        this.goodsConfigID = goodsConfigID;
    }
}
