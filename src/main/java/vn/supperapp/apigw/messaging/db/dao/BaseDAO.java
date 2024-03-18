/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.db.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import javax.transaction.Transactional;

/**
 *
 * @author luandv
 */
public class BaseDAO {
    
    protected Logger logger;
    
    public static boolean checkSession(Session session) {
        return session != null;
    }
    
    public Long getSequence(Session session, String sequenceName) throws Exception {
        String sql = "select " + sequenceName + ".NEXTVAL from dual";
        NativeQuery query = session.createNativeQuery(sql);
        Object seqId = query.uniqueResult();
        if (seqId != null) {
            return Long.parseLong(seqId.toString());
        }
        return null;
    }
    
    protected void logCallMethod(String logName) {
        if (this.logger != null) {
            this.logger.info(logName);
        }
    }

    @Transactional
    public <T> T update(Session session, T obj) throws Exception {
        logger.info("#BaseDAO.update - start");
        try {
            Transaction t = session.getTransaction();
            if (t == null || !t.isActive()) {
                t = session.beginTransaction();
            }
            session.update(obj);
            session.flush();
            t.commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
            logger.error("#BaseDAO.update failed: " , ex);
            throw ex;
        }
        return obj;
    }

    @Transactional
    public <T> T save(Session session, T obj) throws Exception {
        logger.info("#BaseDAO.update - start");
        try {
            Transaction t = session.getTransaction();
            if (t == null || !t.isActive()) {
                t = session.beginTransaction();
            }
            obj = (T) session.save(obj);
            session.flush();
            t.commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
            logger.error("#BaseDAO.save failed: " , ex);
            throw ex;
        }
        return obj;
    }

    @Transactional
    public void delete(Session session, Class<?> type, Long id) {
        logger.info("#BaseDAO.delete - start");
        try {
            Transaction t = session.getTransaction();
            if (t == null || !t.isActive()) {
                t = session.beginTransaction();
            }
            Object persistentInstance = session.load(type, id);
            if (persistentInstance != null) {
                session.delete(persistentInstance);
            }
            session.flush();
            t.commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
            logger.error("#BaseDAO.delete failed: " , ex);
            throw ex;
        }
    }
}
