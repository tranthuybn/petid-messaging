/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.restful.models;

import com.google.gson.Gson;
import org.openide.util.Exceptions;

/**
 * @author luandv
 */
public class BaseRequest {

    private String transId;
    private String signature;
    private String otp;
    private String pin;
    private Long sourceAccountId;
    private String msisdn;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String toLogString() {
        try {
            Gson gson = new Gson();
            String req = gson.toJson(this);
            if (req != null) {
                req = req.replaceAll("(\"pin\":\"[0-9]{6}\")", "\"pin\":\"******\"");
            }
            return req;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        return "";
    }
}
