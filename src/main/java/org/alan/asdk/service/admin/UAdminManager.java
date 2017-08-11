package org.alan.asdk.service.admin;

import org.alan.asdk.cache.impl.manager.TAdminCache;
import org.alan.asdk.dao.manager.TAdminDao;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.utils.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ant on 2015/8/29.
 */

@Service("adminManager")
public class UAdminManager {

    @Autowired
    private TAdminDao adminDao;

    public TAdmin getAdmin(int id) {
        return TAdminCache.getInstance().get(id);
    }

    public TAdmin getAdminByUsername(String username) {
        String hql = "from TAdmin where username = ?";

        return (TAdmin) adminDao.findUnique(hql, username);
    }

    /**
     * 获取用户的子用户
     * @return
     */
    public List<TAdmin> getChildern(int pAdminID){
        return getAllChildren(pAdminID,null);
    }

    /**
     * 判断A是否为B的父级用户
     * @param a 父级用户
     * @param b 子级用户
     * @return true 代表 a 用户是 b 用户的父级用户 .
     */
    public boolean isPUser(TAdmin a , TAdmin b) {
        if (a==null || b ==null){
            return  false;
        }
        if (a.getId().equals(b.getId())){
            return false;
        }
        TAdmin pUser = b.getPUser();
        return pUser != null && (a.getId().equals(pUser.getId()) || isPUser(a, pUser));
    }
    /**
     * 判断A是否为B的父级用户
     * @param a 父级用户ID
     * @param b 子级用户ID
     * @return true 代表 a 用户是 b 用户的父级用户 .
     */
    public boolean isPUser(int a , int b) {
        return isPUser(getAdmin(a),getAdmin(b));
    }

    /**
     * 判断A用户是否为B用户的子级用户
     * @param a 子级用户
     * @param b 父级用户
     * @return true 代表 a用户是b用户的子级用户
     */
    public boolean isCUser(TAdmin a , TAdmin b){
        if (a==null || b ==null){
            return  false;
        }
        if (a.getId().equals(b.getId())){
            return false;
        }
        List<TAdmin> children = getChildern(b.getId());
        for (TAdmin child : children) {
            if (child.getId().equals(a.getId())){
                return true;
            }
        }
        return false;
    }
    /**
     * 判断A用户是否为B用户的子级用户
     * @param a 子级用户ID
     * @param b 父级用户ID
     * @return true 代表 a用户是b用户的子级用户
     */
    public boolean isCUser(int a , int b){
        return isCUser(getAdmin(a),getAdmin(b));
    }

    private List<TAdmin> getAllChildren (int pAdminID , List<TAdmin> result){
        if (result==null){
            result = new ArrayList<>();
        }
        List<TAdmin> admins = TAdminCache.getInstance().getAll();
        for (TAdmin admin : admins) {
            if (Objects.equals(admin.getpAdminID(),pAdminID)){
                result.add(admin);
                getAllChildren(admin.getId(),result);
            }
        }
        return result;
    }

    public String saveAdmain(TAdmin admin) {
        if (admin.getId()==null || admin.getId()==0){
            //查询是否有这个用户名了
            if (getAdminByUsername(admin.getUsername())!=null){
                return "用户名已经存在";
            }
            admin.setId(IDGenerator.getInstance().nextAdminID());
        }else {
            if (StringUtils.isEmpty(admin.getPassword())){
                admin.setPassword(getAdmin(admin.getId()).getPassword());
            }
            //判断是否把父级用户设置为子级用户了
            if(isCUser(admin.getPUser(),admin)){
                return admin.getPUser().getFullName()+"是"+admin.getFullName()+"的下级用户 , 这样做会导致权限混乱!!";
            }
            //判断是否把父级用户和子级用户设置为一个人了
            if(admin.getpAdminID().equals(admin.getId())){
                return "不能把该用户本身设置为其上级用户!";
            }
        }
        TAdminCache.getInstance().save(admin);
        adminDao.save(admin);
        return null;
    }

    public void removeAdmin(int id){
        if (TAdminCache.getInstance().get(id)!=null){
            TAdminCache.getInstance().remove(id);
        }
        adminDao.delete(id);
    }

}
