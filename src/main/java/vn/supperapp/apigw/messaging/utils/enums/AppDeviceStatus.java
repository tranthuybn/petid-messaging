package vn.supperapp.apigw.messaging.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum AppDeviceStatus {
    INIT(1, 1, "Init"),
    LOGGED_IN(2, 2, "Logged in"),
    LOGGED_OUT(3, 3, "Logged out"),
    INACTIVE(4, 4, "inactive"),
    ;

    private final int code;
    private final int value;
    private final String description;

    private static final Map<Integer, AppDeviceStatus> mCodes = new HashMap<>();

    static {
        for (AppDeviceStatus as : AppDeviceStatus.values()) {
            mCodes.put(as.code(), as);
        }
    }

    AppDeviceStatus(int value, int code, String description) {
        this.value = value;
        this.code = code;
        this.description = description;
    }

    public int value() { return value; }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public boolean is(int code) {
        return this.code == code;
    }

}
