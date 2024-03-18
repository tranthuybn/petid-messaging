/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author truonglq
 */
public class CommonUtils {

    static Logger logger = LogManager.getLogger(CommonUtils.class.getName());

    private static final String UNICODE_TEXT_REG = ".*[^\000-].*";
    private static final String BLANK = "";
    private static final String PHONE_NUMBER_SPEC_REG = "[\\{\\}\\(\\) \\+]";
    private static final String TELEPHONE_CODE_PREFIX = "509";
    private static final String TELEPHONE_CODE_ZERO_PREFIX = "00509";

    //    public static final String METFONE_NUMBER_REG = "(00855|855|0|)(31|60|66|67|68|71|88|90|97)\\d{6,7}";
    public static final String METFONE_NUMBER_REG = "(00855|855|0|)((60|66|67|68|90)\\d{6}|(31|71|88|97)\\d{7})";
    public static final String CELLCARD_NUMBER_REG = "(00855|855|0|)((11|14|17|61|77|78|79|85|92|95|99|235)\\d{6}|76\\d{7}|12\\d{6,7})";
    public static final String SMART_NUMBER_REG = "(00855|855|0|)((10|15|16|69|70|81|86|87|93|98)\\d{6}|96\\d{7})";
    public static final String COOTEL_NUMBER_REG = "(00855|855|0|)((38)\\d{7})";
    public static final String SEATEL_NUMBER_REG = "(00855|855|0|)((18|189)\\d{6})";

    public static final String NATCOM_NUMBER_REG = "(00509|509|0|)((22|32|33|40|41|42|43)\\d{6})";
    public static final String DIGICEL_NUMBER_REG = "(00509|509|0|)((28|29|30|31|34|36|37|38|39)\\d{6})";
    public static final String HAITEL_NUMBER_REG = "(00509|509|0|)((25|35)\\d{6})";
    //    public static final String PHONE_NUMBER_REG = "(00855|855|0|)((10|11|13|14|15|16|17|18|60|61|66|67|68|69|70|77|78|79|80|81|83|84|85|86|87|89|90|92|93|95|98|99|189|235)\\d{6}|(31|38|39|71|76|88|96|97)\\d{7}|(12)\\d{6,7})";
    public static final String PHONE_NUMBER_REG = "(00509|509|0|)((22|25|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43)\\d{6})";
    public static final String NUMBER_REG = "\\d+";
    public static final String COMMON_PHONE_NUMBER = "(\\+|0|)(\\d{6,12})";
    public static final String VIETNAM_BANK_ACCOUNT_REG = "\\d{6,35}";
    public static final String BANK_ACCOUNT_NAME_REG = "[a-zA-Z ]{3,50}";
    public static final String PRIVATE_CODE_REG = "\\d{7,8}";
    public static final String LOCAL_BANK_ACCOUNT_REG = "\\d{6,35}";

    public static final Double MAX_AMOUNT = 2000D;
    public static final String RANDOM_PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final String STAFF_CODE_REG = "[a-zA-Z0-9\\._\\-]{3,}";

    public static final String SERVICE_CODE_REG = "[a-zA-Z0-9_\\-/]";
    public static final String PAYMENT_CODE_REG = "^[a-zA-Z0-9\\_\\-\\/]{1,50}$";

    public static final String PIN_REG = "\\d{4}";
    public static final String ACLEDA_OTP_REG = "\\d{6}";
    public static final String USD_REG = "\\d{1,6}(|.\\d{2})";
    public static final String KHR_REG = "\\d{1,8}";

    public static final String TRANS_CONTENT_REG = "^[a-zA-Z \\-\\_\\.]{1,50}$";
    public static final String FULLNAME_REG = "^[a-zA-Z0-9 \\-]{1,50}$";
    public static final String ID_NUMBER_REG = "^[a-zA-Z0-9 \\-]{1,30}$";
    public static final String AMOUNT_REG = "(?!0*\\\\.0*$)[0-9]{1,7}([.][0-9]{0,2}){0,1}";


    public static final String TRANS_REF_ID_REG = "^[a-zA-Z0-9\\-\\_\\.]{1,50}$";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    private static Integer maxAge = 70;
    private static Integer minAge = 14;
    public static final String NIF_NUMBER = "[0-9]{10}";

    private static final SecureRandom random = new SecureRandom();

    public static boolean validate(String reg, String input) {
        Pattern p = Pattern.compile(reg);
        return p.matcher(input).matches();
    }

    public static boolean isNullAny(Object... parameters) {
        for (Object parameter : parameters) {
            if (parameter == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrEmptyAny(String... parameters) {
        for (String parameter : parameters) {
            if (parameter == null || "".equals(parameter.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkFormatAmount(String amount) {
        if (CommonUtils.isNullOrEmpty(amount)) {
            return false;
        }
        return validate(AMOUNT_REG, amount);
    }

    public static String convertDateBirthDay(String dob) {
        try {
            Date birthDate = sdf.parse(dob.replace("/", ""));
            Date curDate = sdf.parse(sdf.format(new Date()));
            Date minDate = changeTime(curDate, -maxAge, Calendar.YEAR);
            Date maxDate = changeTime(curDate, -minAge, Calendar.YEAR);
            if (birthDate.compareTo(maxDate) != 1 && birthDate.compareTo(minDate) != -1) {
                return Utils.getDateStringYYYYMMDD(birthDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date changeTime(Date oriDate, Integer amountChange, Integer timeType) {
        if (amountChange == null || timeType == null) {
            return oriDate;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(oriDate);
        c.add(timeType, amountChange);
        return c.getTime();
    }

    public static <T> T parseJsonToObjectByType(Type tokenType, String json) {
        try {
            return new Gson().fromJson(json, tokenType);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            logger.error("Exception CommonUtils.parseJsonToObject: ", ex);
        }
        return null;
    }

    //<editor-fold defaultstate="collapsed" desc="String Utils">
    public static boolean isNullOrEmptyNotTrim(String input) {
        return input == null || "".equals(input);
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || "".equals(input.trim());
    }

    public static String trim(String input) {
        if (isNullOrEmpty(input)) {
            return BLANK;
        }
        return input.trim();
    }

    public static String toUpperCase(String input) {
        if (!isNullOrEmpty(input)) {
            return input.trim().toUpperCase();
        }
        return input;
    }

    public static String toLowerCase(String input) {
        if (!isNullOrEmpty(input)) {
            return input.trim().toLowerCase();
        }
        return input;
    }

    public static String generatePassword(String dic) {
        String result = "";
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

    public static String generateRandomString(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(RANDOM_PASSWORD.length());
            result += RANDOM_PASSWORD.charAt(index);
        }
        return result;
    }

    public static String generateNumberRandom(int length) {
        String chars = "0123456789";
        String randomstring = "";
        for (int i = 0; i < length; i++) {

            int rnum = (int) Math.floor(Math.random() * chars.length());
            randomstring += chars.substring(rnum, rnum + 1);
        }
        return randomstring;
    }

    public static boolean isUnicodeText(String content) {
        return content.replaceAll("\n|\r", "").matches(UNICODE_TEXT_REG);
    }

    public static String subString(String input, int length) {
        if (!isNullOrEmpty(input) && input.length() > length) {
            return input.substring(0, length);
        }
        return input;
    }

    public static String generateUUID() {
        try {
            UUID uuid = UUID.randomUUID();
            return uuid.toString().replaceAll("-", "");
        } catch (Exception ex) {
            logger.error("ERROR: ", ex);
        }
        return null;
    }

    public static int random(int min, int max) {
        try {
            return min + (int) (Math.random() * ((max - min) + 1));
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Comparator Utils">
    public static boolean compare(Long obj1, Long obj2) {
        return ((obj1 != null) && (obj2 != null) && (obj1.compareTo(obj2) == 0));
    }

    public static boolean compare(String obj1, String obj2) {
        return ((obj1 != null) && (obj2 != null) && obj1.equals(obj2));
    }

    /**
     * Compares two version strings.
     * <p>
     * Use this instead of String.compareTo() for a non-lexicographical comparison that works for version
     * strings. e.g. "1.10".compareTo("1.6").
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a
     * positive integer if str1 is _numerically_ greater than str2. The result is zero if the strings are
     * _numerically_ equal.
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");

        if (vals1.length > vals2.length) {
            String[] tmps = new String[vals1.length];
            for (int k = 0; k < vals1.length; k++) {
                if (k < vals2.length) {
                    tmps[k] = vals2[k];
                } else {
                    tmps[k] = "0";
                }
            }
            vals2 = tmps;
        } else if (vals2.length > vals1.length) {
            String[] tmps = new String[vals2.length];
            for (int k = 0; k < vals2.length; k++) {
                if (k < vals1.length) {
                    tmps[k] = vals1[k];
                } else {
                    tmps[k] = "0";
                }
            }
            vals1 = tmps;
        }

        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Phone number Utils">
    public static boolean isCommonPhoneNumber(String phoneNumber) {
        return validate(COMMON_PHONE_NUMBER, phoneNumber);
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        return validate(PHONE_NUMBER_REG, phoneNumber);
    }

    public static boolean isMetfoneNumber(String input) {
        return validate(METFONE_NUMBER_REG, input);
    }

    public static boolean isSmartNumber(String input) {
        return validate(SMART_NUMBER_REG, input);
    }

    public static boolean isCellCardNumber(String input) {
        return validate(CELLCARD_NUMBER_REG, input);
    }

    public static boolean isCootelNumber(String input) {
        return validate(COOTEL_NUMBER_REG, input);
    }

    public static boolean isSeatelNumber(String input) {
        return validate(SEATEL_NUMBER_REG, input);
    }

    public static boolean isNatcomNumber(String input) {
        return validate(NATCOM_NUMBER_REG, input);
    }

    public static String getMsisdn(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }

        String str = input.replaceAll(PHONE_NUMBER_SPEC_REG, BLANK);

        if (input.startsWith(TELEPHONE_CODE_ZERO_PREFIX)) {
            str = str.substring(2);
        }
        if (input.startsWith(TELEPHONE_CODE_PREFIX)) {
            return str;
        }
//        if (input.startsWith("0")) {
//            str = str.substring(1);
//        }
        return  str;
    }

    public static String getIsdn(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }

        String str = input.replaceAll(PHONE_NUMBER_SPEC_REG, BLANK);

        if (input.startsWith(TELEPHONE_CODE_ZERO_PREFIX)) {
            return str.substring(TELEPHONE_CODE_ZERO_PREFIX.length());
        }
        if (input.startsWith(TELEPHONE_CODE_PREFIX)) {
            return str.substring(TELEPHONE_CODE_PREFIX.length());
        }
        if (input.startsWith("0")) {
            return str.substring(1);
        }

        return str;
    }

    public static String getAccount(String input) {
        if (isNullOrEmpty(input)) {
            return input;
        }

        String str = input.replaceAll(PHONE_NUMBER_SPEC_REG, BLANK);

        if (input.startsWith(TELEPHONE_CODE_ZERO_PREFIX)) {
            return str.substring(TELEPHONE_CODE_ZERO_PREFIX.length());
        }
        if (input.startsWith(TELEPHONE_CODE_PREFIX)) {
            return str.substring(TELEPHONE_CODE_PREFIX.length());
        }

        return str;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Json Utils">
    public static <T> T parseJsonToObject(Class<T> clazz, String json) {
        try {
            return new Gson().fromJson(json, clazz);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            logger.error("Exception CommonUtils.parseJsonToObject: ", ex);
        }
        return null;
    }

    public static String parseObjectToJson(Object obj) {
        try {
            return new Gson().toJson(obj);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            logger.error("Exception CommonUtils.parseObjectToJson: ", ex);
        }
        return BLANK;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Convert Utils">


    public static String convertMoneyToCentSendIso(Double amount) {
        if (amount == null) {
            return null;
        }
        Double amt = amount * 100.00;

        return String.format("%.0f", amt);
    }

    public static Double convertMoneyFromIso(String amount) {
        if (isNullOrEmpty(amount)) {
            return null;
        }
        Double amt = Double.valueOf(amount) / 100.00;

        return amt;
    }

    public static Double round(Double input) {
        return Math.round(input * 100.00) / 100.00;
    }

    public static Double convertKhrToUsd(Double amountKhr, Double rate) {
        Double tot = amountKhr / rate;
        return Math.round(tot * 100.00) / 100.00;
    }

    public static Double convertUsdToKhr(Double amountUsd, Double rate) {
        Double tot = amountUsd * rate;
        return Double.valueOf(Math.round(tot * 100) / 100);
    }


    public static String convertBalanceFromIso(String balance) {
        try {
            if (CommonUtils.isNullOrEmpty(balance)) {
                return "";
            }

//            if (balance.contains("116D")) {
//                return String.format("-%.0f", Double.valueOf(balance.replaceAll("116C", "")) / 100D);
//            } else if (balance.contains("116C")) {
//                return String.format("%.0f", Double.valueOf(balance.replaceAll("116C", "")) / 100D);
//            } else if (balance.contains("840D")) {
//                return String.format("-%.2f", Double.valueOf(balance.replaceAll("840D", "")) / 100D);
//            } else if (balance.contains("840C")) {
//                return String.format("%.2f", Double.valueOf(balance.replaceAll("840C", "")) / 100D);
//            }

            if (balance.contains("332C")) {
                return String.format("%.2f", Double.valueOf(balance.replaceAll("332C", "")) / 100D);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Format Utils">
    public static String formatMoneyWithUnit(Double money, CurrencyCode currency) {
        try {
            if (money == null) {
                return "";
            }

            if (CurrencyCode.VND.compare(currency)) {
                return String.format("%,.2f VND", money);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String formatMoneyWithUnit(String money, CurrencyCode currency) {
        try {
            if (money == null) {
                return "";
            }

            if (CurrencyCode.USD.compare(currency)) {
                return String.format("%,.2f USD", Double.parseDouble(money));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String formatMoneyWithUnit(String money, String currencyNumber) {
        try {
            if (CommonUtils.isNullOrEmpty(money)) {
                return "";
            }

            if (CurrencyCode.USD.compareNumber(currencyNumber)) {
                return String.format("%,.2f USD", Double.valueOf(money));
            } else if (CurrencyCode.VND.compareNumber(currencyNumber)) {
                return String.format("%,.0f VND", Double.valueOf(money));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String formatMoneyWithUnit(Double money, String currencyNumber) {
        try {
            if (money == null) {
                return "";
            }

            if (CurrencyCode.USD.compareNumber(currencyNumber)) {
                return String.format("%,.2f USD", money);
            } else if (CurrencyCode.VND.compareNumber(currencyNumber)) {
                return String.format("%,.0f VND", money);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String formatBalanceWithUnit(String balance) {
        try {
            if (CommonUtils.isNullOrEmpty(balance)) {
                return "";
            }

            if (balance.contains("116D")) {
                return String.format("-%,.0f KHR", Double.valueOf(balance.replaceAll("116C", "")) / 100D);
            } else if (balance.contains("116C")) {
                return String.format("%,.0f KHR", Double.valueOf(balance.replaceAll("116C", "")) / 100D);
            } else if (balance.contains("840D")) {
                return String.format("-%,.2f USD", Double.valueOf(balance.replaceAll("840D", "")) / 100D);
            } else if (balance.contains("840C")) {
                return String.format("%,.2f USD", Double.valueOf(balance.replaceAll("840C", "")) / 100D);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Validator Utils">
    public static boolean validatePin(String pin) {
        if (pin == null) {
            return false;
        }
        return validate(PIN_REG, pin);
    }

    public static boolean validateAcledaOtp(String otp) {
        if (otp == null) {
            return false;
        }
        return validate(ACLEDA_OTP_REG, otp);
    }

    public static boolean validateTransContent(String content) {
        if (isNullOrEmpty(content)) {
            return false;
        }
        return validate(TRANS_CONTENT_REG, content);
    }

    public static boolean validateVietnamBankAccountNumber(String bankAccountNumber) {
        return validate(VIETNAM_BANK_ACCOUNT_REG, bankAccountNumber);
    }

    public static boolean validateBankAccountName(String bankAccountName) {
        if (isNullOrEmpty(bankAccountName)) {
            return false;
        }
        return validate(BANK_ACCOUNT_NAME_REG, bankAccountName);
    }

    public static boolean validatePrivateCode(String privateCode) {
        return validate(PRIVATE_CODE_REG, privateCode);
    }

    public static boolean validateServiceCode(String serviceCode) {
//        if (isNullOrEmpty(serviceCode)) {
//            return false;
//        }
        return validate(SERVICE_CODE_REG, serviceCode);
    }

    public static boolean validatePaymentCode(String paymentCode) {
//        if (isNullOrEmpty(paymentCode)) {
//            return false;
//        }
        return validate(PAYMENT_CODE_REG, paymentCode);
    }

    public static boolean validateLocalBankAccountNumber(String bankAccountNumber) {
        return validate(LOCAL_BANK_ACCOUNT_REG, bankAccountNumber);
    }

    public static boolean validateFullName(String fullName) {
        if (fullName != null) {
            fullName = fullName.trim();
        }
        return validate(FULLNAME_REG, fullName);
    }

    public static boolean validateIdNumber(String idNumber) {
        if (idNumber != null) {
            idNumber = idNumber.trim();
        }
        return validate(ID_NUMBER_REG, idNumber);
    }

    public static boolean validateRefId(String refId) {
        if (refId != null) {
            refId = refId.trim();
        }
        return validate(TRANS_REF_ID_REG, refId);
    }

    //</editor-fold>

    public static String getFileExtInList(String fileName) {
        if (isNullOrEmpty(fileName)) {
            return null;
        }
        String[] strs = fileName.split("\\.");
        if (strs == null) {
            return null;
        }
        String ext = toLowerCase(strs[strs.length - 1]);
//        if (AppConfiguration.getInstance().getExtFileUploadReg().contains(ext)) {
//            return ext;
//        }
        return ext;
    }

    private static void testFuntion() {
        System.out.println("START");
        try {
            System.out.println("PROCESSING");
            return;
        } catch (Exception ex) {

        } finally {
            System.out.println("END");
        }
    }

    public static String getRouteShortcut(String input) {
        String route = "1";
        String[] element = input.split("[\\D+]");
        for (int i = 2; i < element.length; i++) {
            route += element[i];
        }
        return route;
    }

    public static Double getBalanceFromIsoResponseContent(String res) {
        try {
            if (isNullOrEmpty(res)) {
                return null;
            }

            String[] tmps = res.split("\\|MINI STATEMENT DATA\\|");
            String tmp = "";
            if (tmps != null && tmps.length == 2) {
                tmp = tmps[1];
            }

            tmps = tmp.split(",");
            if (tmps != null && tmps.length > 0) {
                tmp = tmps[0];
            }

            tmp = convertBalanceFromIso(tmp);
            return Double.valueOf(tmp);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("# - ERROR: ", ex);
        }
        return null;
    }

    public static Double toDouble(String input) {
        try {
            return Double.valueOf(input);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#toDouble - ERROR: ", ex);
        }
        return null;
    }
    public static boolean validateNif(String nifNum) {
        if (nifNum != null) {
            nifNum = nifNum.trim();
        }
        return validate(NIF_NUMBER, nifNum);
    }

    public static void main(String[] args) {
        if (true) {
            System.out.println(versionCompare("1.10.1", "1.10.1"));
            System.out.println(versionCompare("1.1.1", "1.1.2"));
            System.out.println(versionCompare("1.10", "1.6.1"));
            return;
        }
        if (true) {
            try {
                Long t = System.currentTimeMillis();
                System.out.println(t);
                Thread.sleep(15 * 1000);
                Long t1 = System.currentTimeMillis();
                System.out.println(t1 - t);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        if (true) {

            Gson gson = new Gson();
            Set<String> s = new HashSet<>();
            s.add("test1");
            s.add("test2");
            s.add("test3");

            String tmp = gson.toJson(s);

            System.out.println(tmp);
            Type ts = new TypeToken<Set<String>>() {
            }.getType();
            Set<String> r = gson.fromJson(tmp, ts);

            System.out.println("TEST");
            return;
        }
        if (true) {
            System.out.println(round((15 * 5) / 100D));
            return;
        }
        if (true) {
            String tmp = "0|MTI|0210,2|CARD NUMBER|1001636,3|PROCESS CODE|640008,4|TRANSACTION AMOUNT|000000001190,7|TRANSMISSION DATE/TIME|20210403103117,11|SYSTEM TRACE AUDIT NUMBER|432004,25|SERVICE CODE|EXCHANGE_MONEY,28|TRANSACTION FEE|000000000,32|TELCO CODE|ViettelCam,37|REFERENCE NUMBER|202104039999,39|RESPONSE CODE|00,44|MOBILE NUMBER RECEIVER|855886080886,49|CURRENCY CODE|840,54|BALANCE|840C000000436774,55|VIETTEL REQUEST ID|210403221021228,60|MOBILE NUMBER|855886107442,64|MESSAGE SIGNATURE|8AE0D45C030F2F817F7365CC5791C55A,73|INTEREST_AMOUNT|000005000000,80|TRANSACTION TYPE|1         ,92|ORINGINAL REQUEST ID|210403221021227,100|VIETTEL BANK CODE|FMC,103|CREDIT ACCOUNT NUMBER|1005839,112|MINI STATEMENT DATA|840C000000473188,124|CONTENT DESCRIPTION SERVICE|4200,";
            String[] tmps = tmp.split("\\|MINI STATEMENT DATA\\|");
            for (String it : tmps) {
                System.out.println(it);
            }
            if (tmps != null && tmps.length == 2) {
                tmp = tmps[1];
                tmps = tmp.split(",");
                System.out.println(tmps[0]);
            }
            return;
        }
        if (true) {
            for (int i = 0; i < 100; i++) {
                System.out.println(random(0, 6));
            }
            return;
        }
        if (true) {
            String tmp = "*989*222*5689789*888288688#";
            String r = getRouteShortcut(tmp);
            return;
        }

        if (true) {
            String input = "123456";
            System.out.println(subString(input, 3));
            return;
        }
        if (true) {
            for (int i = 0; i < 10; i++) {
                System.out.println("Task: " + i);
                int count = 0;
                while (true) {
                    if (count < 5) {
                        try {
                            Thread.sleep(1000L);
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        count++;
                    } else {
                        break;
                    }
                }
            }
            return;
        }
        if (true) {
            Gson gson = new Gson();
            List<String> t = new ArrayList<>(Arrays.asList(new String[]{"ABC", "DEF", "XYZ"}));
            System.out.println(gson.toJson(t));
            return;
        }
        if (true) {
            System.out.println(String.format("%,d", 268789L));
            System.out.println(String.format("%,.2f", 124589.867D));
            System.out.println(String.format("%,.0f", 22124589.867D));
            System.out.println(String.format("%,.2f", 3.867D));
            return;
        }
        if (true) {
            System.out.println(validate("^(855)((11|14|17|60|61|66|67|68|77|78|79|85|90|92|95|99|235)\\d{6}|(31|71|76|88|97)\\d{7}|(12)\\d{6,7})$", "855883970777"));
            return;
        }
//        String isdn = "0974110121";
//        System.out.println(getMsisdn(isdn));
//        String[] tmps = "mg.metfone.com.kh".split("@#");
//        System.out.println(validateTransContent("Happy birthday to you .ac"));
//        System.out.println(validateTransContent("test"));
//        System.out.println(validate(PIN_REG, "123456"));
//        System.out.println(validate(PIN_REG, "12345"));
//        System.out.println(validate(PIN_REG, "1234567"));
//        System.out.println(validate(PIN_REG, "000000"));
//        System.out.println(validate(PIN_REG, "abcdef"));
//        System.out.println(validate(CELLCARD_NUMBER_REG, "8551255963258"));

//        System.out.println(validate("\\D", "0"));

//        for (int i = 0; i <100; i++) {
//            System.out.println(generateNumberRandom(8));
//        }
        System.out.println(versionCompare("1.10", "1.10.1"));
        System.out.println(versionCompare("1.10", "1.6.1"));
    }
}
