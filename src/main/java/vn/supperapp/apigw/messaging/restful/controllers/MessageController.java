package vn.supperapp.apigw.messaging.restful.controllers;

import org.glassfish.jersey.jaxb.internal.XmlJaxbElementProvider;
import vn.supperapp.apigw.messaging.beans.AppPushNotificationInfo;
import vn.supperapp.apigw.messaging.beans.ConsumerClientConfigInfo;
import vn.supperapp.apigw.messaging.beans.MessageDataInfo;
import vn.supperapp.apigw.messaging.configs.RsAuthFilterMapping;
import vn.supperapp.apigw.messaging.db.DbSessionMgt;
import vn.supperapp.apigw.messaging.db.dao.MessageDAO;
import vn.supperapp.apigw.messaging.db.dao.UserDAO;
import vn.supperapp.apigw.messaging.db.dto.EndUsers;
import vn.supperapp.apigw.messaging.db.dto.NotificationLog;
import vn.supperapp.apigw.messaging.process.tasks.AppPushNotificationTask;
import vn.supperapp.apigw.messaging.process.tasks.SendSmsTask;
import vn.supperapp.apigw.messaging.restful.models.BaseResponse;
import vn.supperapp.apigw.messaging.restful.models.MessageRequest;
import vn.supperapp.apigw.messaging.utils.Constants;
import vn.supperapp.apigw.messaging.utils.ErrorCode;
import vn.supperapp.apigw.messaging.process.ProcessManager;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import vn.supperapp.apigw.messaging.utils.enums.ChannelType;
import vn.supperapp.apigw.messaging.utils.enums.Language;
import org.hibernate.Session;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/message")
@RsAuthFilterMapping
public class MessageController extends BaseController {

    public MessageController() {
        this.logger = LoggerFactory.getLogger(MessageController.class);
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse sendMessage(MessageRequest request) {
        logApiInfo("sendMessage", "/message/send", request);
        Session session = null;
        UserDAO userDao = null;
        MessageDAO dao = null;
        ConsumerClientConfigInfo consumer = null;
        try {
            consumer = (ConsumerClientConfigInfo) context.getProperty(Constants.CONSUMER_INFO_KEY);
            if (consumer == null) {
                logger.info("#sendMessage - Consumer NOT FOUND");
                return BaseResponse.build(ErrorCode.ERR_COMMON, this.getLanguage());
            }

            if (CommonUtils.isNullOrEmpty(request.getReceiver())) {
                logger.info("#sendMessage - ERR_RECEIVER_INVALID");
                return BaseResponse.build(ErrorCode.ERR_RECEIVER_INVALID, this.getLanguage());
            }
            String receiver = request.getReceiver();

//            List<Long> items = null;
//            String [] strings = receiver.split(",");
//            items = Stream.of(strings).map(Long::valueOf).collect(Collectors.toList());
//            logger.info("#sendMessage - items " + items);


            if (request.getContents() == null || request.getContents().isEmpty()) {
                logger.info("#sendMessage - ERR_CONTENT_INVALID");
                return BaseResponse.build(ErrorCode.ERR_CONTENT_INVALID, this.getLanguage());
            }

            ChannelType channel = ChannelType.get(request.getChannel());
            if (channel == null) {
                channel = ChannelType.APIGW_PET_OWNER;
            }

            Long clientType = 1L;
            if (!ChannelType.APIGW_PET_OWNER.code().equals(channel.code())) {
                clientType = 2L;
            }

            logger.info("#sendMessage Open db session, clientType = %d" + clientType);
            session = DbSessionMgt.shared().getSession();
            logger.info("#sendMessage Check session");
            if (session == null) {
                logger.info("#sendMessage DB Connection error");
                return BaseResponse.commonError(this.getLanguage());
            }

            dao = new MessageDAO();
            userDao = new UserDAO();

            logger.info("#sendMessage - Get MobileUserAccount based on receiver");
            List<EndUsers> receiverUser = userDao.getAppUserBy(session, receiver, clientType);

            logger.info("#sendMessage - receiverUser " + receiverUser.get(0).getMsisdn());

            if(receiverUser == null) return BaseResponse.commonError(this.getLanguage());
            logger.info("#sendMessage - Start to generate message object");
            String receiverLanguage = Language.VIETNAMESE.key();


            for (EndUsers a : receiverUser) {
                if (receiverUser != null && Language.get(a.getLanguage()) != null) {
                    receiverLanguage = a.getLanguage();
                } else if (Language.get(this.getLanguage()) != null) {
                    receiverLanguage = this.getLanguage();
                }
            }

            String receiverContent = request.getContents().get("fullContent");
            String shortContent = request.getContents().get("shortContent");
            String apsTitle = request.getApsTitle(receiverLanguage);

            MessageDataInfo message = new MessageDataInfo();
            message.setSender(request.getSender().trim());
            message.setReceiver(receiver);
            message.setRefId(request.getRefId().trim());
            message.setContent(receiverContent);
            message.setContentLanguage(receiverLanguage);
            message.setContentType(request.getReceiverObj());
            message.setChannel(channel.code());
            if (CommonUtils.isUnicodeText(receiverContent)) {
                message.setUnicode(true);
            } else {
                message.setUnicode(false);
            }
            message.setApsTitle(apsTitle);
            message.setSendSms(request.isSendSms());

            logger.info("#sendMessage - Start to send SMS");
            // SendSmsTask smsTask = new SendSmsTask(consumer, receiverUser, message);
            //ProcessManager.shared().executeTask(SendSmsTask.EXECUTOR_CONFIG_NAME, smsTask);

            logger.info("#sendMessage - Save to NotificationLog table");
            if (request.isSaveNotificationLog()) {
                String jsonData = null;
                String jsonTitle = CommonUtils.parseObjectToJson(request.getTitles());
                if (request.getAppPushNotificationInfo() != null) {
                    jsonData = CommonUtils.parseObjectToJson(request.getAppPushNotificationInfo().getData());
                }
                for(EndUsers appUser : receiverUser)
                {
                    NotificationLog nl = new NotificationLog();
                    nl.setRefId(request.getRefId());
                    nl.setCreateTime(new Date());
                    nl.setMsisdn(appUser.getMsisdn());
                    nl.setEndUserId(appUser.getId());
                    nl.setStatus(0L); //New
                    nl.setNotificationType(0L); //Common
                    nl.setTitle(jsonTitle);
//                    ByteBuffer buffer = StandardCharsets.UTF_8.encode(CommonUtils.parseObjectToJson(request.getContents()));
//                    String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();

                    nl.setShortContent(shortContent);
                    nl.setFullContent(receiverContent);

                    if (request.getNotificationType() != null) {
                        nl.setNotificationType(request.getNotificationType());
                    }

                    if (request.getObjType() != null) {
                        nl.setObjType(request.getObjType());
                    }

                    dao.save(session, nl);
                }

                logger.info("#sendMessage - Check and start to push notification");
                if (request.isPushNotification()) {
                    for(EndUsers a : receiverUser)
                    {
                        int unread = dao.getUnreadNotification(session, a.getMsisdn(), a.getId());
                        a.setUnread(unread);
                    }

                    AppPushNotificationInfo apsInfo = request.getAppPushNotificationInfo();
                    if (apsInfo == null) {
                        apsInfo = new AppPushNotificationInfo();
                    }
                    if (apsInfo.getData() == null) {
                        Map<String, String> data = new HashMap<>();
                        apsInfo.setData(data);
                    }

                    Map<String, String> transId = new HashMap<>();
                    if(request.getTransactionId() != null) {
                        transId.put("transId", request.getTransactionId() + "");
                    }

                    if (!CommonUtils.isNullOrEmpty(request.getRefId())) apsInfo.getData().put("refId", request.getRefId());

                    message.setTransId(transId);
                    message.setApsData(apsInfo.getData());

                    logger.info("#sendMessage - Push notification: " + CommonUtils.parseObjectToJson(message));
                    AppPushNotificationTask notificationTask = new AppPushNotificationTask(consumer, receiverUser, message, clientType);
                    ProcessManager.shared().executeTask(AppPushNotificationTask.EXECUTOR_CONFIG_NAME, notificationTask);
                }

            } else {
                logger.info("#sendMessage - This message NO NEED to save NOTIFICATION LOG");
            }
            return BaseResponse.success(this.getLanguage());
        } catch (Exception ex) {
            logExceptions("#sendMessage - Error: ", ex);
        } finally {
            try {
                DbSessionMgt.shared().closeObject(session);
            } catch (Exception ex) {
                logger.error("#sendMessage close connection error: ", ex);
            }
        }
        return BaseResponse.commonError(this.getLanguage());
    }
}
