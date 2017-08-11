package org.alan.asdk.service;

import org.alan.asdk.common.*;
import org.alan.asdk.dao.logic.UOrderDao;
import org.alan.asdk.dto.PayState;
import org.alan.asdk.entity.UMsdkOrder;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.utils.DateHelper;
import org.alan.asdk.utils.IDGenerator;
import org.alan.asdk.web.SendAgent;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("orderManager")
public class UOrderManager {

    @Autowired
    private UOrderDao orderDao;
    @Autowired
    private DateHelper dateHelper;

    public UOrder getOrder(long orderID) {

        return orderDao.get(orderID);
    }

    public void saveOrder(UOrder order) {
        orderDao.save(order);
    }

    public void saveUMsdkOrder(UMsdkOrder order) {
        orderDao.getSession().saveOrUpdate(order);
    }

    public void deleteOrder(UOrder order) {
        orderDao.delete(order);
    }

    public UOrder generateOrder(UUser user, int money, String productName, String productDesc, String roleID, String roleName, String serverID, String serverName, String extension) {

        UOrder order = new UOrder();
        order.setOrderID(IDGenerator.getInstance().nextOrderID());
        order.setAppID(user.getAppID());
        order.setChannelID(user.getChannelID());
        order.setMoney(money);
        order.setProductName(productName);
        order.setProductDesc(productDesc);
        order.setCurrency("RMB");
        order.setUserID(user.getId());
        order.setUsername(user.getName());
        order.setExtension(extension);
        order.setState(PayState.STATE_PAYING);
        order.setChannelOrderID("");
        order.setRoleID(roleID);
        order.setRoleName(roleName);
        order.setServerID(serverID);
        order.setServerName(serverName);
        order.setCreatedTime(new Date());

        orderDao.save(order);

        return order;
    }

    public UMsdkOrder generateMsdkOrder(UUser user, String channelOrderID, int coinNum, boolean firstPay, int allMoney) {

        UMsdkOrder order = new UMsdkOrder();
        order.setAppID(user.getAppID());
        order.setChannelID(user.getChannelID());
        order.setUserID(user.getId());
        order.setUsername(user.getName());
        order.setCoinNum(coinNum);
        order.setFirstPay(firstPay ? 1 : 0);
        order.setAllMoney(allMoney);
        order.setChannelOrderID(channelOrderID);
        order.setCreatedTime(new Date());

        orderDao.getSession().saveOrUpdate(order);

        return order;
    }

    /**
     * 通过订单状态获取订单
     *
     * @param state       订单状态
     * @param createdTime 订单创建时间
     * @return
     */
    public List<UOrder> getOrderByState(int state, Date createdTime) {
        String hql = "from UOrder order where order.state = :state and order.createdTime >= :beginDate and order.createdTime <= :endDate";
        Session session = orderDao.getSession();
        Query query = session.createQuery(hql);
        query.setInteger("state", state);
        Calendar c = Calendar.getInstance();
        c.setTime(createdTime);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date date = c.getTime();
        query.setTimestamp("beginDate", date);
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        date = c.getTime();
        query.setTimestamp("endDate", date);
        return query.list();
    }

    //分页查找
    public Page<UOrder> queryPage(int currPage, int num) {

        PageParameter page = new PageParameter(currPage, num, true);
        OrderParameters order = new OrderParameters();
        order.add("id", OrderParameter.OrderType.DESC);
        String hql = "from UOrder";
        return orderDao.find(page, hql, null, order);
    }

    public UOrder generateOrder(UUser user, int money, String roleID, String roleName, String serverID, String serverName, String productID, int diamond) {
        UOrder order = new UOrder();
        order.setOrderID(IDGenerator.getInstance().nextOrderID());
        order.setAppID(user.getAppID());
        order.setChannelID(user.getChannelID());
        order.setProductName(productID);
        order.setMoney(money);
        order.setCurrency("RMB");
        order.setUserID(user.getId());
        order.setUsername(user.getName());
        order.setState(PayState.STATE_PAYING);
        order.setChannelOrderID("");
        order.setRoleID(roleID);
        order.setRoleName(roleName);
        order.setServerID(serverID);
        order.setServerName(serverName);
        order.setCreatedTime(new Date());
        order.setDiamond(diamond);
        orderDao.save(order);
        return order;
    }

    public UOrder generateOrderByID(UUser user, int money, String roleID, String roleName, String serverID, String serverName, String productID, int diamond, long orderID) {
        UOrder order = new UOrder();
        order.setOrderID(orderID);
        order.setAppID(user.getAppID());
        order.setChannelID(user.getChannelID());
        order.setProductName(productID);
        order.setMoney(money);
        order.setCurrency("RMB");
        order.setUserID(user.getId());
        order.setUsername(user.getName());
        order.setState(PayState.STATE_PAYING);
        order.setChannelOrderID("");
        order.setRoleID(roleID);
        order.setRoleName(roleName);
        order.setServerID(serverID);
        order.setServerName(serverName);
        order.setCreatedTime(new Date());
        order.setDiamond(diamond);
        orderDao.save(order);
        return order;
    }

    public Page<UOrder> getOrdersByParams(String roleID, long orderID, String channelOrderID) {
        String hql = "";
        String sql = "SELECT orderID, appID, channelID, channelOrderID, currency, extension, money, state, userID, roleID, " +
                "roleName, serverID, serverName, createdTime, productName, productDesc, username, diamond FROM uorder_bak";
        boolean haveParams = false;
        if (orderID != 0) {
            hql = "from UOrder where orderID like '%" + orderID + "%'";
            sql += " where orderID like '%" + orderID + "%'";
            haveParams = true;
        }
        if (!StringUtils.isEmpty(roleID)) {
            hql = "from UOrder where roleID = " + roleID;
            sql += " where roleID = " + roleID;
            haveParams = true;
        }
        if (!StringUtils.isEmpty(channelOrderID)) {
            hql = "from UOrder where channelOrderID like '%" + channelOrderID.trim() + "%'";
            sql += " where channelOrderID like '%" + channelOrderID.trim() + "%'";
            haveParams = true;
        }
        if (StringUtils.isEmpty(hql)) {
            return queryPage(0, 300);
        }
        PageParameter page = new PageParameter(0, 300, true);
        OrderParameters order = new OrderParameters();
        order.add("id", OrderParameter.OrderType.DESC);
        Page<UOrder> orders = orderDao.find(page, hql, null, order);
        if ((orders.getResultList() == null || orders.getResultList().isEmpty()) && haveParams) {
            SQLQuery query = orderDao.getSession().createSQLQuery(sql);
            query.addScalar("orderID", LongType.INSTANCE);
            query.addScalar("appID", IntegerType.INSTANCE);
            query.addScalar("channelID", IntegerType.INSTANCE);
            query.addScalar("userID", IntegerType.INSTANCE);
            query.addScalar("username", StringType.INSTANCE);
            query.addScalar("productName", StringType.INSTANCE);
            query.addScalar("productDesc", StringType.INSTANCE);
            query.addScalar("money", IntegerType.INSTANCE);
            query.addScalar("currency", StringType.INSTANCE);
            query.addScalar("roleID", StringType.INSTANCE);
            query.addScalar("roleName", StringType.INSTANCE);
            query.addScalar("serverID", StringType.INSTANCE);
            query.addScalar("serverName", StringType.INSTANCE);
            query.addScalar("state", IntegerType.INSTANCE);
            query.addScalar("channelOrderID", StringType.INSTANCE);
            query.addScalar("extension", StringType.INSTANCE);
            query.addScalar("createdTime", DateType.INSTANCE);
            query.addScalar("diamond", IntegerType.INSTANCE);
            query.setResultTransformer(new AliasToBeanResultTransformer(UOrder.class));
            List<UOrder> collections = query.list();
            orders.setResultList(collections);
        }
        return orders;
    }

    public UOrder getOrderByChannelOrderID(String channelOrderID) {
        String hql = "from UOrder where channelOrderID = ?";
        return (UOrder) orderDao.findUnique(hql, channelOrderID.trim());
    }

    /**
     * 通过创建时间的范围查找订单
     *
     * @param state     订单状态
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 返回满足该条件的所有订单
     */
    @SuppressWarnings("unchecked")
    public List<UOrder> getOrderByTime(int state, Date beginTime, Date endTime) {
        String hql = "from UOrder order where order.state = :state and order.createdTime >= :beginDate and order.createdTime <= :endDate";
        Session session = orderDao.getSession();
        Query query = session.createQuery(hql);
        query.setInteger("state", state);
        query.setTimestamp("beginDate", beginTime);
        query.setTimestamp("endDate", endTime);
        return query.list();
    }

    @Value("${config.fail.order.sender}")
    private boolean isFailOrderSender;
    /**
     * 判断当前是否正在轮询
     */
    private static boolean haveLoad = false;

    /**
     * 每5分钟执行一次失败订单轮询
     **/
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void loadFailOrders() {
        if (!isFailOrderSender) {
            return;
        }
        if (haveLoad) {
            Log.i("----订单发送器正在检测 , 将在下一次进行轮询----");
        }
        haveLoad = true;
        List<UOrder> orders = getOrderByState(PayState.STATE_FAILED, new Date());
        if (orders != null && !orders.isEmpty()) {
            Log.i("----当次共检测到 :" + orders.size() + " 失败订单 开始通知----");
        }
        for (UOrder order : orders) {
            SendAgent.sendCallbackToServer(this, order);
        }
        Log.i("----失败订单(" + orders.size() + "笔)全部处理完成");
        haveLoad = false;
    }
}
