package org.alan.asdk.dao.logic;

import org.alan.asdk.common.Log;
import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问类
 */
@Repository("userDao")
public class UUserDao extends UHibernateTemplate<UUser, Integer> {

    public UUser findUserByToken(String token){
        String hql = "from UUser where token = '"+token+"'";
        List<UUser> list =  this.find(hql,null,null);
        if(list==null || list.isEmpty()){
            return null;
        }
        if(list.size()>1){
            Log.d("Can not find UUser by token more than 1 the list size is"+list.size());
            return null;
        }
        return list.get(0);
    }

}
