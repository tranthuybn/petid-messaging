/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.db.dao;

import vn.supperapp.apigw.messaging.db.dto.AppDevices;
import vn.supperapp.apigw.messaging.db.dto.EndUsers;
import org.apache.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import vn.supperapp.apigw.messaging.utils.enums.AppDeviceStatus;

import java.util.List;

/**
 *
 * @author luand
 */
public class UserDAO extends BaseDAO {

    public UserDAO() {
        this.logger = LogManager.getLogger(UserDAO.class);
    }

    public  List<EndUsers> getAppUserBy(Session session, String msisdn, Long clientType) throws Exception {
        logger.info("#getAppUserBy: accountNumber") ;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" From EndUsers au ");
            sql.append(" Where au.msisdn = :msisdn ");
            sql.append("   And au.clientType = :clientType ");

            Query query = session.createQuery(sql.toString());
            query.setParameter("msisdn", msisdn);
            query.setParameter("clientType", clientType);

            return query.list();
        } catch (Exception ex) {
            logger.error("#getAppUserBy ERROR ", ex);
            throw ex;
        }
    }

    public List<AppDevices> getListActiveDeviceSendNotification(Session session, List<Long> endUserId) throws Exception {
        logger.info("#getListActiveDeviceSendNotification");
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" From AppDevices ad ");
            sql.append(" Where ad.endUserId = :endUserId ");
            sql.append("   And ad.status = :status ");

            Query query = session.createQuery(sql.toString());
            query.setParameterList("endUserId", endUserId);
            query.setParameter("status", AppDeviceStatus.LOGGED_IN.value());

            return query.list();
        } catch (Exception ex) {
            logger.error("#getListActiveDeviceSendNotification ERROR ", ex);
            throw ex;
        }
    }

}
