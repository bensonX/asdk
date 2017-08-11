package org.alan.asdk.service;

import org.alan.asdk.common.*;
import org.alan.asdk.dao.logic.UUserDao;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**

 */
@Service("userManager")
public class UUserManager {

    @Autowired
    private UUserDao userDao;

    /**
     * 根据渠道用户ID获取用户信息
     */
    public UUser getUserByCpID(int appID, int channelID, String cpUserID) {

        String hql = "from UUser where appID = ? and channelID = ? and channelUserID = ?";

        return (UUser) userDao.findUnique(hql, appID, channelID, cpUserID);

    }

    /**
     * 获取指定渠道下的所有用户
     * @param channelID
     * @return
     */
    public List<UUser> getUsersByChannel(int channelID) {
        String hql = "from UUser where channelID = ?";

        return userDao.find(hql, new Object[]{channelID}, null);
    }

    //获取用户数量
    public long getUserCount() {
        String hql = "select count(id) from UUser";
        return userDao.findLong(hql, null);
    }

    //分页查找
    public Page<UUser> queryPage(int currPage, int num) {

        PageParameter page = new PageParameter(currPage, num, true);
        OrderParameters order = new OrderParameters();
        order.add("id", OrderParameter.OrderType.DESC);
        String hql = "from UUser";
        return userDao.find(page, hql, null, order);
    }


    public UUser getUser(int userID) {
        return userDao.get(userID);
    }

    /**
     * 验证签名是否合法(不区分大小写)
     * @param signStr 正确的签名字符串(不能转换为MD5)
     * @param sign 待验证的签名字符串
     * @return Boolean 匹配则返回true
     */
    public boolean isSignOK(String signStr, String sign) {

//        Log.i("*****************************"+signStr +" , "+sign);
        String newSign = EncryptUtils.md5(signStr);
        boolean flag = newSign.toLowerCase().equals(sign.toLowerCase());
        if(!flag){
            Log.i("Sign["+sign+"]verify failed , the signStr = "+signStr+" , the right sign is["+newSign+"] , sign is error");
        }
        return flag;

    }

    public boolean checkUser(UUser user, String token) {
        if (user == null){
            return false;
        }
        long now = System.currentTimeMillis();
        if (!token.equals(user.getToken()) || (now - user.getLastLoginTime().getTime()) > 3600 * 10000) {
            return false;
        }
        return user.getToken().equals(token);

    }

    /**
     *
     * @param token
     * @return
     */
    public UUser getUUserByToken(String token){
        if (token==null){
            return null;
        }

//        UUser user = UPlayerCache.getInstance().getUUser(token);
//        if (user==null){
//            String hql = "from UUser where token = '"+token+"'";
//            List<UUser> list =  userDao.find(hql,null,null);
//            if(list==null || list.isEmpty()){
//                Log.d("Can not find UUser by token : "+ "'"+token+"'");
//                return null;
//            }
//            if(list.size()>1){
//                Log.d("Can not find UUser by token more than 1 the list size is"+list.size());
//                return null;
//            }
//            user =list.get(0);
//            if (user!=null){
//                UPlayerCache.getInstance().saveUUser(user);
//            }
//        }
       return userDao.findUserByToken(token);
    }

    public UUser generateUser(UChannel channel, SDKVerifyResult cpUserInfo) {

        UUser user = new UUser();
        user.setAppID(channel.getAppID());
        user.setChannelID(channel.getChannelID());
        user.setName(System.currentTimeMillis() + channel.getMaster().getNameSuffix());
        user.setChannelUserID(cpUserInfo.getUserID());
        user.setChannelUserName(cpUserInfo.getUserName());
        user.setChannelUserNick(cpUserInfo.getNickName());
        Date now = new Date();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setExtension(cpUserInfo.getExtension());
//        userDao.save(user);

        return user;
    }

    public void saveUser(UUser user) {
//        UPlayerCache.getInstance().saveUUser(user);
        userDao.save(user);
    }

    public void deleteUser(UUser user) {
        userDao.delete(user);
    }
}
