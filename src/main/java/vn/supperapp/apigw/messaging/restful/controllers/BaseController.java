/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.restful.controllers;

import com.google.gson.Gson;
import vn.supperapp.apigw.messaging.db.dao.BaseDAO;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import org.hibernate.Session;
import org.slf4j.Logger;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.text.DecimalFormat;

/**
 * @author luandv
 */
public class BaseController {

    protected Logger logger;

    protected String language;

    @Context
    protected ContainerRequestContext context;

    @HeaderParam("language")
    public void setLanguage(String language) {
        if (CommonUtils.isNullOrEmpty(language)) {
            this.language = "ht";
        } else {
            this.language = language;
        }
    }

    public String getLanguage() {
        return language;
    }

    protected void logApiInfo(String method, String api, Object request) {
        if (logger != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n############################################################################################");
            sb.append("\n# method: " + method);
            sb.append("\n# api: " + api);
            if (request != null) {
                sb.append("\n# request: " + toJsonString(request));
            }
            sb.append("\n############################################################################################");
            logger.info(sb.toString());
        }
    }

    protected void logApiInfo(String method, String api, Object... request) {
        if (logger != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--------------------------------------------------------------------------------\n");
            sb.append("\n- method: " + method);
            sb.append("\n- api: " + api);
            if (request != null) {
                sb.append("\n- request: " + getParamLogItem(request));
            }
            sb.append("\n--------------------------------------------------------------------------------\n");
            logger.info(sb.toString());
        }
    }

    protected void logExceptions(String message, Exception ex) {
        if (ex != null) {
            ex.printStackTrace();
        }
        logger.error(message, ex);
    }

    protected String getParamLogItem(Object... params) {
        if (params == null || params.length == 0) {
            return "";
        }
        StringBuilder sbLogs = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            sbLogs.append("\n").append(params[i]).append(": ");
            if (i + 1 < params.length) {
                sbLogs.append(String.valueOf(params[i + 1]));
            }
        }
        return sbLogs.toString();
    }

    protected String toJsonString(Object obj) {
        try {
            Gson gson = new Gson();
            String req = gson.toJson(obj);
            if (req != null) {
                req = req.replaceAll("(\"pin\":\"[0-9]{6}\")", "\"pin\":\"******\"");
            }
            return req;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    protected boolean checkDbSession(Session session) {
//        return BaseExtraAccDAO.checkSession(session);
//    }

    public String convertNumber(Double number) {
        logger.info("#convertNumber - START");
        try {
            DecimalFormat formatter = new DecimalFormat("###,###,###.##");
            return formatter.format(number);
        } catch (Exception e) {
            logger.info("#convertNumber - ERROR", e);
            e.printStackTrace();
        }
        return number.toString();
    }

    protected boolean checkDbSession(Session session) {
        return BaseDAO.checkSession(session);
    }

}
