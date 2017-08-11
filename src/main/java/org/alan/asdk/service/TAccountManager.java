package org.alan.asdk.service;

import org.alan.asdk.dao.logic.UAccountDao;
import org.alan.asdk.entity.UAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015-12-03.
 */
@Service("accountManager")
public class TAccountManager {

    @Autowired
    private UAccountDao dao;

    /**
     * 增加账号
     * @param appID appID
     * @param channelID channelID
     * @param username 用户名
     * @param psw 密码MD5后
     * @param email 邮箱地址
     * @param realPsw 真实密码
     * @return 账号对象
     */
    public UAccount generateAccount(int appID, int channelID, String username, String psw, String email , String realPsw){
        UAccount account = new UAccount();
        account.setAppID(appID);
        account.setChannelID(channelID);
        account.setUsername(username);
        account.setPsw(psw);
        account.setRealPsw(realPsw);
        account.setEmail(email);
        saveTAccount(account);
        return account;
    }

    public void saveTAccount(UAccount account){
        dao.save(account);
    }

    public UAccount getAccountByLoginInfo(String username, String psw){
        String hql = "from UAccount where username = ? and psw = ?";
        return (UAccount)dao.findUnique(hql, username, psw);
    }
    public UAccount getAccountByUserName(String username){
        String hql = "from UAccount where username = ?";
        return (UAccount)dao.findUnique(hql, username);
    }


    public UAccount getAccountByMail(String mail){
        String hql = "from UAccount where email = ?";
        return (UAccount)dao.findUnique(hql,mail);
    }

}
