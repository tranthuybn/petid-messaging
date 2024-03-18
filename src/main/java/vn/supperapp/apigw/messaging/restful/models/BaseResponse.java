/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.restful.models;


import com.google.gson.Gson;
import vn.supperapp.apigw.messaging.utils.CurrencyCode;
import vn.supperapp.apigw.messaging.utils.ErrorCode;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import vn.supperapp.apigw.messaging.utils.LanguageUtils;
import org.openide.util.Exceptions;

/**
 * @author luandv
 */
public class BaseResponse {
    private int status;
    private String code;
    private String message;

    private String transId;
    private String refId;
    private String transactionTime;
    private boolean requireOtp = false;
    private int otpExpiredInSecond = 60;
    private String otpMessage;
    private String balance;
    private String currency = CurrencyCode.VND.number();
    private String currencyCode = CurrencyCode.VND.code();

    public BaseResponse() {
    }

    public BaseResponse(int status, String code) {
        this.status = status;
        this.code = code;
        this.message = "";
    }

    public BaseResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public BaseResponse(ErrorCode error, String language) {
        this.status = error.status();
        this.code = error.code();
        this.message = LanguageUtils.getString(this.code, language);
        if (CommonUtils.isNullOrEmpty(this.message)) {
            this.message = error.message();
        }
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRequireOtp() {
        return requireOtp;
    }

    public void setRequireOtp(boolean requireOtp) {
        this.requireOtp = requireOtp;
    }

    public int getOtpExpiredInSecond() {
        return otpExpiredInSecond;
    }

    public void setOtpExpiredInSecond(int otpExpiredInSecond) {
        this.otpExpiredInSecond = otpExpiredInSecond;
    }

    public String getOtpMessage() {
        return otpMessage;
    }

    public void setOtpMessage(String otpMessage) {
        this.otpMessage = otpMessage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public static BaseResponse success(String language) {
        return new BaseResponse(ErrorCode.SUCCESS, language);
    }

    public static BaseResponse commonError(String language) {
        return new BaseResponse(ErrorCode.ERR_COMMON, language);
    }

    public static BaseResponse missingParameters(String language) {
        return new BaseResponse(ErrorCode.ERR_MISSING_PARAMETERS, language);
    }

    public static BaseResponse build(ErrorCode error, String language) {
        return new BaseResponse(error, language);
    }

    public void setErrorCode(ErrorCode errorCode, String language) {
        this.status = errorCode.status();
        this.code = errorCode.code();
        this.message = LanguageUtils.getString(this.code, language);
    }

    public static BaseResponse build(String responseCode, String language) {
        BaseResponse response = new BaseResponse();
        String msg = LanguageUtils.getString(responseCode, language);
        if (CommonUtils.isNullOrEmpty(msg)) {
            response.setErrorCode(ErrorCode.ERR_COMMON, language);
        } else {
            response.setStatus(Integer.valueOf(responseCode));
            response.setCode(responseCode);
            response.setMessage(msg);
        }
        return response;
    }

    public String toLogString() {
        try {
            Gson gson = new Gson();
            String tmp = gson.toJson(this);
            if (tmp != null) {
                tmp = tmp.replaceAll("(\"pin\":\"[0-9]{6}\")", "\"pin\":\"******\"");
            }
            return tmp;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        return "";
    }
}
