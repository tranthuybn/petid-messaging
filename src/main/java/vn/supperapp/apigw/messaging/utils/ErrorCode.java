/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luand
 */
public enum ErrorCode {
    SUCCESS(0, "MSG_SUCCESS", "Successfully"),
    ERR_COMMON(1, "ERR_COMMON", "System error, please try again!"),
    ERR_MISSING_PARAMETERS(2, "ERR_MISSING_PARAMETERS", "Missing parameters"),
    ERR_PARAMETERS_INVALID(2, "ERR_PARAMETERS_INVALID", "Missing parameters"),
    ERR_TRANSACTION_EXPIRED(3, "ERR_TRANSACTION_EXPIRED", "Transaction expired"),
    ERR_RESEND_OTP_OVER_LIMITED(5, "ERR_RESEND_OTP_OVER_LIMITED", "Cannot resend OTP, over limited"),

    ERR_REFID_INVALID(5, "ERR_REFID_INVALID", "Please enter valid reference ID"),
    ERR_SENDER_INVALID(5, "ERR_SENDER_INVALID", "Please enter valid sender"),
    ERR_RECEIVER_INVALID(5, "ERR_RECEIVER_INVALID", "Please enter valid receiver"),
    ERR_CONTENT_INVALID(5, "ERR_CONTENT_INVALID", "Please enter valid content"),

    ERR_RECEIVER_CARRIER_NOT_IN_CONFIG(5, "ERR_RECEIVER_CARRIER_NOT_IN_CONFIG", "Receiver carrier not in configuration"),
    RR_SENDER_NOT_IN_CONFIG(5, "RR_SENDER_NOT_IN_CONFIG", "Sender not in configuration"),
    ERR_SENDER_SOURCE_NOT_FOUND(5, "ERR_SENDER_SOURCE_NOT_FOUND", "Client sender source not found"),

    ;

    private final int status;
    private final String code;
    private final String message;

    private static final Map<Integer, ErrorCode> mStatusValues = new HashMap<Integer, ErrorCode>();
    private static final Map<String, ErrorCode> mCodeValues = new HashMap<String, ErrorCode>();

    static {
        for (ErrorCode ec : ErrorCode.values()) {
            mStatusValues.put(ec.status(), ec);
            mCodeValues.put(ec.code(), ec);
        }
    }

    private ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int status() {
        return this.status;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public boolean is(int status) {
        return this.status == status;
    }

    public static ErrorCode get(int status) {
        return mStatusValues.get(status);
    }

    public static ErrorCode get(String code) {
        return mCodeValues.get(code);
    }
}
