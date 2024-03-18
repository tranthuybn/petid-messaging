/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.process.tasks;

import vn.supperapp.apigw.messaging.beans.ConsumerClientConfigInfo;
import vn.supperapp.apigw.messaging.beans.MessageDataInfo;
import vn.supperapp.apigw.messaging.clients.firebase.FirebaseClient;
import vn.supperapp.apigw.messaging.clients.firebase.FirebaseClientAgent;
import vn.supperapp.apigw.messaging.db.DbSessionMgt;
import vn.supperapp.apigw.messaging.db.dao.BaseDAO;
import vn.supperapp.apigw.messaging.db.dao.MessageDAO;
import vn.supperapp.apigw.messaging.db.dao.UserDAO;
import vn.supperapp.apigw.messaging.db.dto.AppDevices;
import vn.supperapp.apigw.messaging.db.dto.EndUsers;
import vn.supperapp.apigw.messaging.db.dto.MessageLog;
import vn.supperapp.apigw.messaging.process.tasks.BaseTask;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luand
 */
public class AppPushNotificationTask extends BaseTask {
    public static final String EXECUTOR_CONFIG_NAME = "appPushNotificationExecutor";

    private ConsumerClientConfigInfo consumer;
    private List<EndUsers> user;
    private MessageDataInfo message;
    private Long clientType;
    //Tạo object data và nhận dữ liệu thông quan constructor


    public AppPushNotificationTask(ConsumerClientConfigInfo consumer, List<EndUsers> user, MessageDataInfo message, Long clientType) {
        this.logger = LoggerFactory.getLogger(AppPushNotificationTask.class);
        this.consumer = consumer;
        this.user = user;
        this.message = message;
        this.clientType = clientType;
    }

    @Override
    public void run() {
       // logger.info("#run - Start AppPushNotificationTask ");
        String LOG_TAG = message != null ? message.getRefId() : String.valueOf(System.currentTimeMillis());
        Session session = null;
        MessageDAO dao = null;
        UserDAO userDao = null;
        try {
            if (message == null || consumer == null || user == null) {
                logger.info("{} - INVALID DATA", LOG_TAG);
                return;
            }

            logger.info("{} Open db app session", LOG_TAG);
            session = DbSessionMgt.shared().getSession();
            logger.info("{} Check session", LOG_TAG);
            if (!BaseDAO.checkSession(session)) {
                logger.info("{} DB Connection error", LOG_TAG);
                return;
            }
            dao = new MessageDAO();
            userDao = new UserDAO();

            List<Long> ids = new ArrayList<>();
            for (EndUsers a : user){
                ids.add(a.getId());
            }
            logger.info("{} - Get list logged in device to send notification");
            List<AppDevices> devices = userDao.getListActiveDeviceSendNotification(session, ids);
            if (devices == null || devices.isEmpty()) {
                logger.info("{} - No devices to send notification", LOG_TAG);
                return;
            }

            List<MessageLog> logs = new ArrayList<>();
            for (AppDevices it : devices) {
                if (CommonUtils.isNullOrEmpty(it.getFirebaseToken())) {
                    logger.info("{} - Device {} has no firebase token", LOG_TAG, it.getDeviceId());
                    continue;
                }

                logger.info("{} - Save message log for device = {}", LOG_TAG, it.getDeviceId());
                for (EndUsers a : user)
                {

                    MessageLog ml = new MessageLog(message, "aps");
                    ml.setFirebaseToken(it.getFirebaseToken());
                    ml.setEndUserId(a.getId());
                    ml.setUnRead(a.getUnread());
                    logs.add(ml);
                }

            }

            logger.info("{} - Start to push notification", LOG_TAG);
            if (logs.isEmpty()) {
                logger.info("{} - No device to push notification", LOG_TAG);
                return;
            }

            if (logs.size() == 1) {
                String rs = "";
                logger.info("{} - Has one device only, send single message", LOG_TAG);
                MessageLog ml = logs.get(0);
                if(clientType == 2)
                {
                    logger.info("{} - start push to agent", clientType);
                    rs = FirebaseClientAgent.shared().send(ml.getFirebaseToken(), message.getApsData(), message.getApsTitle(), message.getContent());
                }
                else
                {
                    logger.info("{} - start push to EU", clientType);
                    rs = FirebaseClient.shared().send(ml.getFirebaseToken(), message.getApsData(), message.getApsTitle(), message.getContent());
                }
                ml.setObjResponse(CommonUtils.subString(rs, 3000));
                logger.info("{} - Save message log to db", LOG_TAG);
                dao.save(session, ml);
            } else {
                logger.info("{} - Has multiple devices, send multicast message", LOG_TAG);
                if(clientType == 2) {
                    logger.info("{} - start push to agent", clientType);
                    FirebaseClientAgent.shared().sendMultiple(logs, message.getApsData(), message.getApsTitle(), message.getContent());
                }
                else
                {
                    logger.info("{} - start push to EU", clientType);
                    FirebaseClient.shared().sendMultiple(logs, message.getApsData(), message.getApsTitle(), message.getContent());
                }
                logger.info("{} - Save message log to db", LOG_TAG);
                for (MessageLog ml : logs) {
                    dao.save(session, ml);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                DbSessionMgt.shared().closeObject(session);
            } catch (Exception ex) {
                logger.error(LOG_TAG + " close connection error: ", ex);
            }
        }
    }


}
