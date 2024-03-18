package vn.supperapp.apigw.messaging.restful.filters;

import vn.supperapp.apigw.messaging.beans.ConsumerClientConfigInfo;
import vn.supperapp.apigw.messaging.configs.AppConfigurations;
import vn.supperapp.apigw.messaging.configs.RsAuthFilterMapping;
import vn.supperapp.apigw.messaging.utils.Constants;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Priority(100)
@RsAuthFilterMapping
public class RsAuthFilter implements ContainerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(RsAuthFilter.class);

    @Context
    HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        logger.info("############################# RsAuthFilter - ContainerRequestFilter ########################");
        try {
            //Log header
            MultivaluedMap<String, String> headers = crc.getHeaders();
            if (headers != null) {
                StringBuilder strLog = new StringBuilder();
                strLog.append("\n###################[ Header ####################");
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    List<String> value = entry.getValue();
                    strLog.append(String.format("\n# %s: %s", key, value));
                }
                strLog.append("\n###################] Header ####################");
                logger.info(strLog.toString());
            }

            String authorizationHeader = CommonUtils.trim(crc.getHeaderString(HttpHeaders.AUTHORIZATION));
            if (CommonUtils.isNullOrEmpty(authorizationHeader)) {
                logger.info("Device ID or Authorization Header is null");
                crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
                return;
            }

            String[] tmps = authorizationHeader.split(" ");
            if (tmps == null || tmps.length != 2) {
                logger.info("Device ID or Authorization Header invalid");
                crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
                return;
            }

            String clientName = CommonUtils.trim(tmps[0]);
            String apiKey = CommonUtils.trim(tmps[1]);
            if (CommonUtils.isNullOrEmpty(clientName) || CommonUtils.isNullOrEmpty(apiKey)) {
                logger.info("#filter - Authorization Header is invalid");
                crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
                return;
            }

            ConsumerClientConfigInfo clientInfo = AppConfigurations.shared().getConsumer(clientName);
            if (clientInfo == null) {
                logger.info("#filter - Not found client info in configuration");
                crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
                return;
            }
            if (!apiKey.equals(clientInfo.getApiKey())) {
                logger.info("#filter - API KEY is invalid");
                crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
                return;
            }

            //Check IP
            String clientIp = getClientIp(httpServletRequest);
            logger.info("#filter - Client IP = {}", clientIp);
            if (CommonUtils.isNullOrEmpty(clientIp)) {
                logger.info("#filter - Client IP is not recognize");
                crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
                return;
            }

            //TODO: Check ip in restriction
            //TODO: Check validity

            logger.info("#Set Consumer to request");
            crc.setProperty(Constants.CONSUMER_INFO_KEY, clientInfo);
        } catch (Exception ex) {
            logger.error("#filter - ERROR: ", ex);
        }
    }

    private static final String[] HEADERS_LIST = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getClientIp(HttpServletRequest request) {
        for (String header : HEADERS_LIST) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
