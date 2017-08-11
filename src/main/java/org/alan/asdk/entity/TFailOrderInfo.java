package org.alan.asdk.entity;

/**
 * 失败订单包装类
 *
 * @author Lance Chow
 * @create 2015-12-21 15:53
 */
public class TFailOrderInfo {
    /**
     * 订单对象
     */
    private UOrder order;
    /**
     * 下一次发送订单的时间 long值
     */
    private long next;
    /**
     * 已经发送了多少次了
     */
    private int count;

    /**
     * 当前订单是否正在处理
     */
    private boolean isRuning;

    public UOrder getOrder() {
        return order;
    }

    public void setOrder(UOrder order) {
        this.order = order;
    }

    public long getNext() {
        return next;
    }

    public void setNext(long next) {
        this.next = next;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isRuning() {
        return isRuning;
    }

    public void setRuning(boolean runing) {
        isRuning = runing;
    }
}
