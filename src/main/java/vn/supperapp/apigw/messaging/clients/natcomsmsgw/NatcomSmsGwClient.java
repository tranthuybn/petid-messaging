package vn.supperapp.apigw.messaging.clients.natcomsmsgw;

import vn.supperapp.apigw.messaging.clients.IdleConnectionMonitor;
import vn.supperapp.apigw.messaging.clients.InsecureHostnameVerifier;
import vn.supperapp.apigw.messaging.clients.natcomsmsgw.objs.SendSmsResponse;
import vn.supperapp.apigw.messaging.configs.smsgw.SmsGwConfig;
import vn.supperapp.apigw.messaging.configs.smsgw.SmsGwProfileInfo;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.openide.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import vn.supperapp.apigw.messaging.utils.Hex;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NatcomSmsGwClient {
    final static Logger logger = LoggerFactory.getLogger(NatcomSmsGwClient.class);
    private static volatile NatcomSmsGwClient instance;
    private static Object mutex = new Object();
    private static final String CONFIG_PATH = "natcom-smsgw-config.yml";
    private CloseableHttpClient httpClient;
    private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private IdleConnectionMonitor monitor;

    SmsGwConfig config;

    public NatcomSmsGwClient() {
        try {
            logger.info("Init NatcomSmsGwClient");
            //TODO: Do something to init

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static NatcomSmsGwClient shared() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new NatcomSmsGwClient();
                    instance.loadConfig();
                }
            }
        }
        return instance;
    }

    private void loadConfig() {
        try {
            logger.info("#loadConfig - Read config from file: {}", CONFIG_PATH);
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource(CONFIG_PATH);

            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(resource.getPath());
            this.config = yaml.loadAs(inputStream, SmsGwConfig.class);

            logger.info("#loadConfig - Init connection manager with configuration");
            cm.setMaxTotal(this.config.getMaxTotal());
            cm.setDefaultMaxPerRoute(this.config.getMaxConnectionPerHost());

            logger.info("#loadConfig - Init timeout config");
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(this.config.getConnectionTimeout())
                    .setConnectionRequestTimeout(this.config.getConnectionRequestTimeout())
                    .setSocketTimeout(this.config.getSoTimeout()).build();

            logger.info("#loadConfig - build client");
            HttpClientBuilder builder = HttpClients.custom()
                    .setDefaultRequestConfig(config)
                    .setRedirectStrategy(new LaxRedirectStrategy())
                    .setSSLHostnameVerifier(new InsecureHostnameVerifier())
                    .setConnectionManager(cm)
                    ;

            httpClient = builder.build();

            logger.info("#loadConfig - build close idle connection monitor");
            monitor = new IdleConnectionMonitor(cm);
            // Start up the monitor.
            Thread monitorThread = new Thread(monitor);
            monitorThread.setDaemon(true);
            monitorThread.start();

        } catch (Exception ex) {
            logger.error("loadConfig - ERROR: ", ex);
            Exceptions.printStackTrace(ex);
        }
    }

    public void shutdownMonitor() throws InterruptedException, IOException {
        if (httpClient != null) {
            httpClient.close();
        }
        if (this.monitor != null) {
            this.monitor.shutdown();
        }
    }

    public SmsGwProfileInfo getProfile(String profileName) {
        return this.config.getProfile(profileName);
    }

    public SendSmsResponse sendSms(String profileName, String refId, String receiver, String content) {
        logger.info("#sendSms - START: profileName = {}; refId = {}; receiver = {}", profileName, refId, receiver);
        long timeStart = System.currentTimeMillis();
        HttpPost hPost = null;
        CloseableHttpResponse hRes = null;
        SendSmsResponse response = new SendSmsResponse(0, "Success");
        try {
            logger.info("#sendSms - Load sms Profile");
            SmsGwProfileInfo profile = this.config.getProfile(profileName);
            if (profile == null) {
                logger.info("#sendSms - No profile found");
                return new SendSmsResponse(-1, "No SMS Profile found");
            }

            String smsContent = "";
            byte[] byteContent = content.getBytes("UTF-16BE");
            smsContent = Hex.encode(byteContent);

            logger.info("#sendSms - Api URL: {}", profile.getUrl());
            hPost = new HttpPost(profile.getUrl());
            hPost.addHeader("Content-Type", "application/xml; charset=utf8");
            hPost.addHeader("Accept", "*/*");

            logger.info("#sendSms - generate request");
            StringBuilder requestBody = new StringBuilder();
            requestBody.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            requestBody.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            requestBody.append("   <soap:Header>");
            requestBody.append("      <AuthHeader xmlns=\"http://tempuri.org/\">");
            requestBody.append("         <Username>").append(profile.getUsername()).append("</Username>");
            requestBody.append("         <Password>").append(profile.getPassword()).append("</Password>");
            requestBody.append("      </AuthHeader>");
            requestBody.append("   </soap:Header>");
            requestBody.append("   <soap:Body>");
            requestBody.append("      <sendMT xmlns=\"http://tempuri.org/\">");
            requestBody.append("         <SessionId>0</SessionId>");
            requestBody.append("         <ServiceId>warning</ServiceId>");
            requestBody.append("         <Sender>").append(profile.getAlias()).append("</Sender>");
            requestBody.append("         <Receiver>").append(receiver).append("</Receiver>");
            requestBody.append("         <ContentType>1</ContentType>");
            requestBody.append("         <Content>").append(smsContent).append("</Content>");
            requestBody.append("         <Status>1</Status>");
            requestBody.append("      </sendMT>");
            requestBody.append("   </soap:Body>");
            requestBody.append("</soap:Envelope>");

            logger.info("#sendSms - REQUEST: " + requestBody.toString());

            StringEntity entity = new StringEntity(requestBody.toString());
            hPost.setEntity(entity);

            logger.info("#sendSms: START TO SENDING REQUEST TO SERVER");
            hRes = httpClient.execute(hPost);

            logger.info("#sendSms: EXECUTE SOAP SUCCESS - check HTTP STATUS");
            if (hRes == null || hRes.getStatusLine().getStatusCode() != 200) {
                logger.info("#sendSms HTTP STATUS: {}", hRes.getStatusLine().getStatusCode());
                int st = hRes != null ? hRes.getStatusLine().getStatusCode() : -1;
                return new SendSmsResponse(st, "Http Error");
            }

            logger.info("#sendSms: EXECUTE SOAP SUCCESS - start to get response");
            String res = EntityUtils.toString(hRes.getEntity());

            if (CommonUtils.isNullOrEmpty(res)) {
                logger.info("#sendSms RESPONSE IS NULL");
                return new SendSmsResponse(-1, "Response string is null");
            }

            logger.info("#sendSms RESPONSE: SUCCESS");
            response.setResponse(res);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#sendSms - EXCEPTION: ", ex);
            response.setStatus(-1);
            response.setMessage("Exception");
        } finally {
            if (hPost != null) {
                hPost.releaseConnection();
            }
            if (hRes != null) {
                try {
                    hRes.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        logger.info("#sendSms END - Time : " + (System.currentTimeMillis() - timeStart));
        return response;
    }

    public static void main(String[] args) {
        try {
//            NatcomSmsGwClient.shared().sendSms();
            System.out.println("TEST");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
