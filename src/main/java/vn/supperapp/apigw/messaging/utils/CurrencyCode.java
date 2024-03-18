package vn.supperapp.apigw.messaging.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author truonglq
 */
public enum CurrencyCode {
    USD("USD","840",  "United States dollar"),
    VND("VND","704",  "Vietnamese đồng"),
    ;

    private final String code;
    private final String number;
    private final String description;

    private static final Map<String, CurrencyCode> mCodeValues = new HashMap<String, CurrencyCode>();
    private static final Map<String, CurrencyCode> mNumberValues = new HashMap<String, CurrencyCode>();

    static {
        for (CurrencyCode ccy : CurrencyCode.values()) {
            mCodeValues.put(ccy.code(), ccy);
            mNumberValues.put(ccy.number(), ccy);
        }
    }

    CurrencyCode(String code, String number, String description) {
        this.code = code;
        this.number = number;
        this.description = description;
    }

    public String code() {
        return this.code;
    }
    public String number() {
        return this.number;
    }
    public String description() {
        return this.description;
    }


    public boolean compare(CurrencyCode currency) {
        return currency != null && this.compareTo(currency) == 0;
    }
    public boolean compareCode(String code) {
        return this.code.equals(code);
    }
    public boolean compareNumber(String number) {
        return this.number.equals(number);
    }

    public static CurrencyCode get(String code) {
        return mCodeValues.get(code);
    }

    public static CurrencyCode getByNumber(String number) {
        return mNumberValues.get(number);
    }

}
