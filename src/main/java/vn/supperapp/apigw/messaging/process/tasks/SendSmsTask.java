/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.process.tasks;

import vn.supperapp.apigw.messaging.beans.ConsumerClientConfigInfo;
import vn.supperapp.apigw.messaging.beans.MessageDataInfo;
import vn.supperapp.apigw.messaging.clients.natcomsmsgw.NatcomSmsGwClient;
import vn.supperapp.apigw.messaging.configs.smsgw.SmsGwProfileInfo;
import vn.supperapp.apigw.messaging.db.DbSessionMgt;
import vn.supperapp.apigw.messaging.db.dao.BaseDAO;
import vn.supperapp.apigw.messaging.db.dao.MessageDAO;
import vn.supperapp.apigw.messaging.db.dto.EndUsers;
import vn.supperapp.apigw.messaging.db.dto.MessageLog;
import vn.supperapp.apigw.messaging.process.tasks.BaseTask;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

/**
 * @author luand
 */
public class SendSmsTask extends BaseTask {
    public static final String EXECUTOR_CONFIG_NAME = "sendSmsExecutor";

    private ConsumerClientConfigInfo consumer;
    private EndUsers user;
    private MessageDataInfo message;
    //Tạo object data và nhận dữ liệu thông quan constructor

    public SendSmsTask(ConsumerClientConfigInfo consumer, EndUsers user, MessageDataInfo message) {
        this.logger = LoggerFactory.getLogger(SendSmsTask.class);
        this.consumer = consumer;
        this.user = user;
        this.message = message;
    }

    @Override
    public void run() {
        logger.info("#run - Start SendSmsTask ");
        String LOG_TAG = message != null ? message.getRefId() : String.valueOf(System.currentTimeMillis());
        Session session = null;
        MessageDAO dao = null;
        try {
            if (message == null || consumer == null) {
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

            logger.info("{} - Generate Message Log", LOG_TAG);
            MessageLog ml = new MessageLog(message, "sms");

            SmsGwProfileInfo profile = NatcomSmsGwClient.shared().getProfile(consumer.getSmsProfileName());
            ml.setSender(profile.getAlias());
            if (user != null) {
                ml.setEndUserId(user.getId());
            }

            logger.info("{} - Save messagge log", LOG_TAG);
            dao.save(session, ml);

            logger.info("{} - Start to send SMS", LOG_TAG);
            boolean willSendSms = this.message.isSendSms();
            if (consumer.isCheckSmsReceiverWhitelist() && !consumer.checkReceiverInWhitelist(message.getReceiver())) {
                logger.info("{} - Receiver {} not in whitelist, SMS will not send", LOG_TAG, message.getReceiver());
                willSendSms = false;
            }

            if (willSendSms) {
                NatcomSmsGwClient.shared().sendSms(consumer.getSmsProfileName(), message.getRefId(), message.getReceiver(), message.getContent());
            }

        } catch (Exception ex) {
            logger.error(LOG_TAG + "- EXCEPTION: ", ex);
        } finally {
            try {
                DbSessionMgt.shared().closeObject(session);
            } catch (Exception ex) {
                logger.error(LOG_TAG + " close connection error: ", ex);
            }
        }
    }


}
