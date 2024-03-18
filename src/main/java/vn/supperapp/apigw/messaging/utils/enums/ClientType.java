/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author truonglq
 */
public enum ClientType {
    UNKNOWN(-1L, "UNKNOWN", "UNKNOWN"),
    BOTH(0L, "BOTH", "BOTH"),
    END_USER_APP(1L, "END_USER_APP", "USER"),
    AGENT_MERCHANT_APP(2L, "AGENT_MERCHANT_APP", "AGENT MERCHANT APP"),
    ;

    private final Long value;
    private final String code;
    private final String description;

    private static final Map<Long, ClientType> mValues = new HashMap<Long, ClientType>();
    private static final Map<String, ClientType> mCodeValues = new HashMap<String, ClientType>();

    static {
        for (ClientType ccy : ClientType.values()) {
            mValues.put(ccy.value(), ccy);
            mCodeValues.put(ccy.code(), ccy);
        }
    }

    private ClientType(Long value, String code, String description) {
        this.value = value;
        this.code = code;
        this.description = description;
    }

    public Long value() {
        return this.value;
    }

    public String code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }

    public boolean is(Long value) {
        return this.value == value;
    }

    public static ClientType get(Long value) {
        return mValues.get(value);
    }

    public static ClientType get(String code) {
        return mCodeValues.get(code);
    }
    
}
