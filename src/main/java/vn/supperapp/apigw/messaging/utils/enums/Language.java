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
public enum Language {
    ENGLISH(0, "en", "English"),
    VIETNAMESE(1, "vi", "Việt Nam"),
    ;

    private final int value;
    private final String key;
    private final String description;

    private static final Map<Integer, Language> mValues = new HashMap<Integer, Language>();
    private static final Map<String, Language> mKeyValues = new HashMap<String, Language>();

    static {
        for (Language lg : Language.values()) {
            mValues.put(lg.value(), lg);
            mKeyValues.put(lg.key(), lg);
        }
    }

    private Language(int value, String key, String description) {
        this.value = value;
        this.key = key;
        this.description = description;
    }

    public int value() {
        return this.value;
    }

    public String key() {
        return this.key;
    }

    public String description() {
        return this.description;
    }

    public boolean is(int value) {
        return this.value == value;
    }
    public boolean is(String key) {
        return this.key.equals(key);
    }

    public static Language get(int value) {
        return mValues.get(value);
    }

    public static Language get(String key) {
        return mKeyValues.get(key);
    }
    
    public static Language defaultLanguage() {
        return ENGLISH;
    }

    public static boolean contains(String key) {
        return mKeyValues.containsKey(key);
    }
}
