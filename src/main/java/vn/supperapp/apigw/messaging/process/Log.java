package vn.supperapp.apigw.messaging.process;

import org.apache.log4j.Logger;

public class Log {
    private static Logger internal;

    private Log() {
    }

    public static void setLogger(Logger logger) {
        internal = logger;
    }

    public static void debug(Object message) {
        if (internal != null) {
            internal.debug(message);
        }

    }

    public static void debug(Object message, Throwable t) {
        if (internal != null) {
            internal.debug(message, t);
        }

    }

    public static void info(Object message) {
        if (internal != null) {
            internal.info(message);
        }

    }

    public static void info(Object message, Throwable t) {
        if (internal != null) {
            internal.info(message, t);
        }

    }

    public static void warn(Object message) {
        if (internal != null) {
            internal.warn(message);
        }

    }

    public static void warn(Object message, Throwable t) {
        if (internal != null) {
            internal.warn(message, t);
        }

    }

    public static void error(Object message) {
        if (internal != null) {
            internal.error(message);
        }

    }

    public static void error(Object message, Throwable t) {
        if (internal != null) {
            internal.error(message, t);
        }

    }

    public static void fatal(Object message) {
        if (internal != null) {
            internal.fatal(message);
        }

    }

    public static void fatal(Object message, Throwable t) {
        if (internal != null) {
            internal.fatal(message, t);
        }

    }

    static {
        try {
            internal = Logger.getLogger("mmserver");
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
