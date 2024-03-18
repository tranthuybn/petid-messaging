package vn.supperapp.apigw.messaging.clients.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import vn.supperapp.apigw.messaging.clients.firebase.objs.ApsMultiResponse;
import vn.supperapp.apigw.messaging.configs.firebase.FirebaseConfig;
import vn.supperapp.apigw.messaging.db.dto.MessageLog;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import org.openide.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirebaseClientAgent {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseClientAgent.class);

    private static final String CONFIG_PATH = "./firebase-config.yml";

    private static volatile FirebaseClientAgent instance;
    private static Object mutex = new Object();

    private FirebaseConfig config;

    public static FirebaseClientAgent shared() {
        if (instance == null || instance.config == null) {
            synchronized (mutex) {
                if (instance == null || instance.config == null) {
                    instance = new FirebaseClientAgent();
                    instance.loadConfig();
                }
            }
        }
        return instance;
    }

    private void loadConfig() {
        try {
            logger.info("#loadConfig - Read config from file: {}", CONFIG_PATH);

            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(CONFIG_PATH);
            this.config = yaml.loadAs(inputStream, FirebaseConfig.class);

            FileInputStream serviceAccount = new FileInputStream(config.getCredentialFileAgent());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(this.config.getDatabaseUrlAgent())
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception ex) {
            logger.error("loadConfig - ERROR: ", ex);
            Exceptions.printStackTrace(ex);
        }
    }

    public String send(String token, Map<String, String> apsData, String title, String content) {
        logger.info("#send - Start send single message");
        try {
            if (apsData == null) {
                apsData = new HashMap<>();
            }
            Notification noti = new Notification(title,content);
            Message message = Message.builder()
                    .putAllData(apsData)
                    .setNotification(noti)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(this.config.getTtl())
                            .setNotification(AndroidNotification.builder().setColor(this.config.getColor()).setSound(this.config.getSound()).build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder().setBadge(1).setSound(this.config.getSound()).build())
                            .build())
                    .setToken(token)
                    .build();
            logger.info("#send - Start to send");
            String fbRes = FirebaseMessaging.getInstance().send(message);
            logger.info("#send - fbres:", fbRes);
            return fbRes;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#send - EXCEPTION: ", ex);
        }
        return "";
    }

    public List<ApsMultiResponse> sendMultiple(List<MessageLog> messageLogs, Map<String, String> apsData, String title, String content) {
        logger.info("#send - Start send single message");
        try {
            if (apsData == null) {
                apsData = new HashMap<>();
            }

            List<String> tokens = messageLogs.stream().map(MessageLog::getFirebaseToken).collect(Collectors.toList());

            Notification noti = new Notification(title,content);
            MulticastMessage message = MulticastMessage.builder()
                    .putAllData(apsData)
                    .setNotification(noti)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(this.config.getTtl())
                            .setNotification(AndroidNotification.builder().setColor(this.config.getColor()).setSound(this.config.getSound()).build())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder().setBadge(1).setSound(this.config.getSound()).build())
                            .build())
                    .addAllTokens(tokens)
                    .build();

            logger.info("#run - Start to send");
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

            List<SendResponse> responses = response.getResponses();
            List<ApsMultiResponse> lstRes = new ArrayList<>();
            int i = 0;
            MessageLog ml = null;
            for (SendResponse it : responses) {
                try {
                    logger.info("FBRES: {} - {}", it.getMessageId(), it.isSuccessful());
                    ApsMultiResponse res = new ApsMultiResponse();
                    res.setMessageId(it.getMessageId());
                    ml = messageLogs.get(i);
                    res.setToken(ml.getFirebaseToken());
                    i++;
                    if (it.isSuccessful()) {
                        res.setStatus(0); //Success
                        res.setMessage("Success");
                    } else {
                        res.setStatus(1); //Error
                        res.setMessage(it.getException() != null ? it.getException().getMessage() : null);
                    }
                    ml.setObjResponse(CommonUtils.subString(res.getMessage(), 3000));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("#send - Process response error", ex);
                }
            }
            return lstRes;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#send - EXCEPTION: ", ex);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        try {
            if (false) {
                String token = "eFFaJXqmjkg-qJabNVH1vs:APA91bFlfJC5Ok_9Gfwx0717gmunvzNqc-3ejOLjAzJB2JUMUwID4iXAMEanKF-G5yXUDDF6n9Y-cwvDJxyOjYpSOg0u_PdpLHrvPWLUjxcyuaeXsqNCx3BFfYXIKk3GB0NU-1Ganv36";

                FirebaseClientAgent.shared().send(token, null, "Test", "Test content here");
            }

            if (true) {
                String token = "eFFaJXqmjkg-qJabNVH1vs:APA91bFlfJC5Ok_9Gfwx0717gmunvzNqc-3ejOLjAzJB2JUMUwID4iXAMEanKF-G5yXUDDF6n9Y-cwvDJxyOjYpSOg0u_PdpLHrvPWLUjxcyuaeXsqNCx3BFfYXIKk3GB0NU-1Ganv36";
                MessageLog ml = new MessageLog();
                ml.setFirebaseToken(token);
                List<MessageLog> logs = new ArrayList<>();
                logs.add(ml);

                FirebaseClientAgent.shared().sendMultiple(logs, null, "Received money", "Received 100,000 HTG from abc xyz");
                System.out.println("TEST");
            }
            System.out.println("END");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
