/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author luandv
 */
public class DbSessionMgt {

    private static final Logger logger = LoggerFactory.getLogger(DbSessionMgt.class);

    public static final String DB_BANKPLUS = "DB_BANKPLUS";
    public static final String DB_APP = "DB_APP";
    public static final String DB_APP1 = "DB_APP1";
    public static final String DB_MERCHANT = "DB_MERCHANT";
    public static final String DB_IM = "DB_IM";
    public static final String DB_EMONEY_LOG = "DB_EMONEY_LOG";
    public static final String DB_EMONEY_APP_SYNC = "DB_EMONEY_APP_SYNC";

    private static volatile DbSessionMgt instance;
    private static Object mutex = new Object();

    public static DbSessionMgt shared() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new DbSessionMgt();
                }
            }
        }
        return instance;
    }

    private Map<String, SessionFactory> mapSessionFactory = new ConcurrentHashMap<>();
    
    public void initDefaultDbSession(SessionFactory appSessionFactory) {
        mapSessionFactory.put(DB_APP, appSessionFactory);
    }

    public void addDbSession(String dbAlias, SessionFactory appSessionFactory) {
        mapSessionFactory.put(dbAlias, appSessionFactory);
    }
    
    public Session getSession() throws Exception {
        return getSession(DB_APP);
    }
    
    public Session getSession(String key) throws Exception {
        Session session = null;
        try {
            SessionFactory sessionFactory = mapSessionFactory.get(key);
            session = (sessionFactory != null) ? sessionFactory.openSession()
                    : null;

            return session;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Session getDbBankPlusSession() throws Exception {
        Session session = null;
        try {
            SessionFactory sessionFactory = mapSessionFactory.get(DB_BANKPLUS);
            session = (sessionFactory != null) ? sessionFactory.openSession() : null;

            return session;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

//    
//    public static Long getSequence(Session session, String sequenceName) throws Exception {
//        String sql = "select " + sequenceName + ".NEXTVAL from dual";
//        SQLQuery query = session.createSQLQuery(sql);
//        List lst = query.list();
//        if (lst.size() == 0) {
//            return null;
//        }
//        return Long.parseLong(lst.get(0).toString());
//    }
    
    public void closeObject(Session obj) {
        try {
            if (obj != null && obj.isOpen()) {//Ket qua review
                obj.close();
                obj = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#closeObject - EXCEPTION: ", e);
        }
    }
    
    public void rollbackObject(Session session) {//Ket qua review
        try {
            if (session != null && session.isOpen()) {//Ket qua review
                Transaction t = session.getTransaction();//Ket qua review
                if (t != null && t.isActive()) {
                    t.rollback();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("#rollbackObject - EXCEPTION: ", e);
        }
    }
    
    public void commitObject(Session session) throws Exception {//Ket qua review
        try {
            if (session != null && session.isOpen()) {
                Transaction t = session.getTransaction();
                if (t != null && t.isActive()) {
                    t.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
