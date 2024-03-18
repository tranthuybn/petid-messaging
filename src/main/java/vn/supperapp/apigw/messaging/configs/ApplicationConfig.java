package vn.supperapp.apigw.messaging.configs;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ApplicationConfig {
    private static final Logger logger = Logger.getLogger(ApplicationConfig.class);
    private static volatile ApplicationConfig instance;
    private List<Integer> signISOList;
    private String countryCode;
    private String countryLocale;
    private String trunkCode;
    private String symboy0;
    private String symboy1;
    private String expireToken;
    private String strCurrency;
    private Map mapCurrency;
    private Map mapPosition;
    private String senderSms;
    private String serIndiSubPre;
    private String serIndiSubPos;
    private String serIndiEdtl;
    private String currencyNumCode;
    private String currencyCode;
    private String ipCoreapp;

    public ApplicationConfig() {
    }

    public String getIpCoreapp() {
        return this.ipCoreapp;
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            Class var0 = ApplicationConfig.class;
            synchronized(ApplicationConfig.class) {
                if (instance == null) {
                    try {
                        instance = new ApplicationConfig();
                        instance.loadConfig();
                    } catch (Exception var3) {
                        logger.error("Error init application config!", var3);
                    }
                }
            }
        }

        return instance;
    }

    private void loadConfig() throws Exception {
        logger.info("=======loadConfig============");
        Properties properties = new Properties();
        properties.load(new FileInputStream("../etc/application.cfg"));
        String[] signList = properties.getProperty("sign.iso.list").trim().split(",");
        this.signISOList = this.convertArrayStringToArrayInt(signList);
        this.countryCode = properties.getProperty("country.code").trim();
        this.trunkCode = properties.getProperty("trunk.code");
        this.countryLocale = properties.getProperty("country.locale");
        this.symboy0 = properties.getProperty("symbol.position.default0");
        this.symboy1 = properties.getProperty("symbol.position.default1");
        this.strCurrency = properties.getProperty("currency.map").trim();
        this.mapCurrency = this.loadMapCurrency(properties.getProperty("currency.map").trim());
        this.mapPosition = this.loadMapPosition(properties.getProperty("currency.position").trim());
        this.senderSms = properties.getProperty("smsSender.alias").trim();
        this.ipCoreapp = properties.getProperty("server.address").trim();

        try {
            this.currencyNumCode = properties.getProperty("currency.numcode").trim();
        } catch (Exception var9) {
            logger.warn("currency.numcode is null", var9);
        }

        try {
            this.currencyCode = properties.getProperty("currency.code").trim();
        } catch (Exception var8) {
            logger.warn("currency.numcode is null", var8);
        }

        try {
            this.expireToken = properties.getProperty("token.expire").trim();
        } catch (Exception var7) {
            logger.warn("token.expire is null", var7);
        }

        try {
            this.serIndiSubPre = properties.getProperty("service.indicator.subpre").trim();
        } catch (Exception var6) {
            logger.warn("service indicator sub pre is null", var6);
        }

        try {
            this.serIndiSubPos = properties.getProperty("service.indicator.subpos").trim();
        } catch (Exception var5) {
            logger.warn("service indicator sub pos is null", var5);
        }

        try {
            this.serIndiEdtl = properties.getProperty("service.indicator.edtlcard").trim();
        } catch (Exception var4) {
            logger.warn("service indicator electric card is null", var4);
        }

        logger.info("Get application config success.");
    }

    private Map loadMapCurrency(String strCurrency) {
        String[] arrCurrency = strCurrency.split(";");
        Map hashmapCurrency = new HashMap();
        if (arrCurrency != null) {
            String[] var4 = arrCurrency;
            int var5 = arrCurrency.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String arrCurrency1 = var4[var6];
                String[] objCurrency = arrCurrency1.split(":");
                hashmapCurrency.put(objCurrency[0], objCurrency[1]);
            }
        }

        return hashmapCurrency;
    }

    private List<Integer> convertArrayStringToArrayInt(String[] stringArray) {
        List<Integer> intArray = new ArrayList();
        String[] var3 = stringArray;
        int var4 = stringArray.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            intArray.add(Integer.parseInt(str.trim()));
        }

        return intArray;
    }

    public String getSymboy0() {
        return this.symboy0;
    }

    public String getSymboy1() {
        return this.symboy1;
    }

    public List<Integer> getSignISOList() {
        return this.signISOList;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCountryLocale() {
        return this.countryLocale;
    }

    public String getTrunkCode() {
        return this.trunkCode == null ? "" : this.trunkCode;
    }

    public Map getMapCurrency() {
        return this.mapCurrency;
    }

    public String getSenderSms() {
        return this.senderSms;
    }

    public String getSerIndiSubPre() {
        return this.serIndiSubPre;
    }

    public String getSerIndiSubPos() {
        return this.serIndiSubPos;
    }

    public String getSerIndiEdtl() {
        return this.serIndiEdtl;
    }

    public Map getMapPosition() {
        return this.mapPosition;
    }

    public void setMapPosition(Map mapPosition) {
        this.mapPosition = mapPosition;
    }

    private Map loadMapPosition(String strCurrencyP) {
        String[] arrCurrency = strCurrencyP.split(",");
        Map hashmapCurrency = new HashMap();
        if (arrCurrency != null) {
            String[] var4 = arrCurrency;
            int var5 = arrCurrency.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String arrCurrency1 = var4[var6];
                String[] objCurrency = arrCurrency1.split(":");
                hashmapCurrency.put(objCurrency[0], objCurrency[1]);
            }
        }

        return hashmapCurrency;
    }

    public String getExpireToken() {
        return this.expireToken;
    }

    public String getCurrencyNumCode() {
        return this.currencyNumCode;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }
}
