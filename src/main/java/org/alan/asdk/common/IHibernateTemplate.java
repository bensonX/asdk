package org.alan.asdk.common;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public interface IHibernateTemplate<T, PK extends Serializable> {
    Session getSession();

    T get(PK id);

    void save(T data);

    void delete(T data);

    void delete(PK id);

    List<T> findAll(OrderParameters orderParams);

    Criteria getCriteria();

    List<T> find(String sql, Object[] params, OrderParameters orderParams);

    Page<T> find(PageParameter page, String sql, Object[] params, OrderParameters orderParams);

    Query createQuery(String sql, Object[] params);

    Object findUnique(String sql, Object[] params);

    Integer findInt(String sql, Object[] params);

    Long findLong(String sql, Object[] params);

    SessionFactory getSessionFactory();
}