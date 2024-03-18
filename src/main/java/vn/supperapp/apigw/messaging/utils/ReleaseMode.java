/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luand
 */
public enum ReleaseMode {
    UNKNOWN(-1, "UNKNOWN", "UNKNOWN"),
    DEVELOPMENT(1, "dev", "DEVELOPMENT"),
    TESTING(2, "test", "TESTING"),
    PRODUCTION(3, "production", "PRODUCTION"),
    ;

    private final int value;
    private final String code;
    private final String description;

    private static final Map<Integer, ReleaseMode> mValues = new HashMap<Integer, ReleaseMode>();
    private static final Map<String, ReleaseMode> mCodeValues = new HashMap<String, ReleaseMode>();

    static {
        for (ReleaseMode ccy : ReleaseMode.values()) {
            mValues.put(ccy.value(), ccy);
            mCodeValues.put(ccy.code(), ccy);
        }
    }

    private ReleaseMode(int value, String code, String description) {
        this.value = value;
        this.code = code;
        this.description = description;
    }

    public int value() {
        return this.value;
    }

    public String code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }

    public boolean is(int value) {
        return this.value == value;
    }

    public boolean is(String mode) {
        return this.code.equals(mode);
    }

    public static ReleaseMode get(int value) {
        return mValues.get(value);
    }

    public static ReleaseMode get(String code) {
        return mCodeValues.get(code);
    }
    
}
