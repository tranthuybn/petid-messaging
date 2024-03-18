package vn.supperapp.apigw.messaging.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import vn.supperapp.apigw.messaging.configs.ApplicationConfig;

public class Utils {
    private static final Locale LOCAL_GERMAN;
    public static final String DATE_FORMAT_FULL = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT_DMY = "ddMMyyyy";
    public static final String DATE_FORMAT_FULL_DISPLAY = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT_FULL_DISPLAY_TEMPLATE = "ddMMyyyyHHmmss";
    public static final String DATE_FORMAT_DMY_DISPLAY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_FULL_DISPLAY1 = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_FULL_DISPLAY2 = "HH:mm dd/MM/yyyy";
    private static final Locale LOCAL_EN;
    static CharsetEncoder asciiEncoder;

    public Utils() {
    }

    public static String getCurrentDateFullString() {
        return getDateString(Calendar.getInstance().getTime(), "yyyyMMddHHmmss");
    }

    public static String formatAmountToString(Double amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        return formatter.format(amount);
    }

    public static int getPosSymbol(String input, String language) {
        if (input != null) {
            String[] langPos = input.split(",");
            String[] var3 = langPos;
            int var4 = langPos.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String anA = var3[var5];
                if (anA.split(":")[0].equalsIgnoreCase(language)) {
                    return Integer.parseInt(anA.split(":")[1]);
                }
            }
        }

        return 0;
    }

    public static String formatCurrency(String input, String lang, String[] symbol, int symbolPosition) {
        Double amountDouble = formatStringISOToDouble(input);
        if (amountDouble == null) {
            amountDouble = 0.0;
        }

        DecimalFormat decimalFormat = (DecimalFormat)DecimalFormat.getNumberInstance(LOCAL_EN);
        return decimalFormat.format(amountDouble) + " " + symbol[0];
    }

    public static String getDateString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static String getDateStringYYYYMMDD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static String getDateStringDDMMYYYY(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        return sdf.format(date);
    }

    public static String getDateStringDateFull(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    public static boolean validateByRegex(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).matches();
    }

    public static String padLeft(String text, int totalWidth, Character paddingChar) {
        String strReturn = text;
        if (text.length() < totalWidth) {
            int numAdd = totalWidth - text.length();

            for(int i = 0; i < numAdd; ++i) {
                strReturn = paddingChar.toString() + strReturn;
            }
        }

        return strReturn;
    }

    public static String formatDoubleToString(Double amount) {
        String strAmount = String.format("%.2f", amount);
        String strDouble = strAmount.replace(".", "");
        return strDouble;
    }

    public static String formatStringToString(String amountIso) {
        Double amount1 = formatStringISOToDouble(amountIso);
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(LOCAL_EN);
        String amountOut = numberFormatter.format(amount1);
        return amountOut;
    }

    public static String formatStringDoubleToString(String amountDouble) {
        Double amount1 = Double.parseDouble(amountDouble);
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(LOCAL_EN);
        String amountOut = numberFormatter.format(amount1);
        return amountOut;
    }

    public static String formatDoubleStringAmount(Double amount) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(LOCAL_EN);
        String amountOut = numberFormatter.format(amount);
        return amountOut;
    }

    public static String formatAmount(Double amount) {
        return padLeft(formatDoubleToString(amount), 15, '0');
    }

    public static String formatISOFee(Double fee) {
        return padLeft(formatDoubleToString(fee), 15, '0');
    }

    public static String formatISOBonus(Double bonus) {
        return padLeft(formatDoubleToString(bonus), 15, '0');
    }

    public static String formatISODiscount(Double discount) {
        return padLeft(formatDoubleToString(discount), 15, '0');
    }

    public static String formatBalance(Double balance, String currency) {
        String code = balance < 0.0 ? "D" : "C";
        String strBalance = formatDoubleToString(balance);
        strBalance = currency + code + strBalance;
        return strBalance;
    }

    public static Date converStringToDateFull(String date) {
        return convertStringToTime(date, "yyyyMMddHHmmss");
    }

    public static Date converStringToDateDMY(String date) {
        return convertStringToTime(date, "ddMMyyyy");
    }

    public static Date converStringToDateYMD(String date) {
        return convertStringToTime(date, "yyyyMMdd");
    }

    public static String getDateStringFullDisplay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public static String getDateStringFullDisplay1(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }

    public static String getDateStringFullDisplay2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String getDateStringFullDisplayTemplate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        return sdf.format(date);
    }

    public static String getDateStringDMYDisplay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static Date convertStringToTime(String date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(date);
        } catch (ParseException var4) {
        } catch (Exception var5) {
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        String csvFile = "C:/Users/hoangtt10/Desktop/ivdata.csv";
        BufferedReader br = null;
        String cvsSplitBy = " ";

        try {
            br = new BufferedReader(new FileReader(csvFile));

            String line;
            while((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);
                System.out.println("Country [code= " + country[0] + " , name=" + country[1] + "]");
            }
        } catch (FileNotFoundException var16) {
        } catch (IOException var17) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var15) {
                }
            }

        }

    }

    public static Double formatStringISOToDouble(String isoDouble) {
        if (isoDouble != null && !"".equals(isoDouble)) {
            Double dIso = Double.parseDouble(isoDouble.substring(0, isoDouble.length() - 2) + "." + isoDouble.substring(isoDouble.length() - 2));
            return dIso;
        } else {
            return 0.0;
        }
    }

    public static Long formatStringISO4DecimalToLong(String isoDoubleFull) {
        if (isoDoubleFull != null && !"".equals(isoDoubleFull)) {
            Long dIso = Long.parseLong(isoDoubleFull.substring(isoDoubleFull.length() - 2));
            return dIso;
        } else {
            return 0L;
        }
    }

    public static String refineMobileNumber(String msisdn) {
        String countryCode = ApplicationConfig.getInstance().getCountryCode();
        if (msisdn.startsWith("+".concat(countryCode))) {
            msisdn = msisdn.substring(1);
        }

        if (!"".equals(msisdn) && !msisdn.startsWith(countryCode) && msisdn.length() < 9) {
            if (String.valueOf(msisdn.charAt(0)).equals(ApplicationConfig.getInstance().getTrunkCode())) {
                return countryCode + msisdn.substring(1, msisdn.length());
            }

            if (msisdn.length() > countryCode.length() && !msisdn.substring(0, countryCode.length()).equals(countryCode)) {
                return countryCode + msisdn;
            }
        }

        return msisdn;
    }

    public static String unRefineMobileNumber(String msisdn) {
        String countryCode = ApplicationConfig.getInstance().getCountryCode();
        int lengthCode = countryCode.length();
        return msisdn != null && msisdn.length() > 5 && msisdn.substring(0, lengthCode).equals(countryCode) ? ApplicationConfig.getInstance().getTrunkCode() + msisdn.substring(lengthCode, msisdn.length()) : msisdn;
    }

    public static String convertMsisdnToIsdn(String msisdn) {
        String countryCode = ApplicationConfig.getInstance().getCountryCode();
        String trunkCode = ApplicationConfig.getInstance().getTrunkCode();
        if (msisdn.startsWith(countryCode)) {
            msisdn = msisdn.substring(countryCode.length(), msisdn.length());
        }

        if (msisdn.startsWith(trunkCode)) {
            msisdn = msisdn.substring(trunkCode.length(), msisdn.length());
        }

        return msisdn;
    }

    public static String convertMsisdnOrIsdnToPhone(String phone) {
        String countryCode = ApplicationConfig.getInstance().getCountryCode();
        String trunkCode = ApplicationConfig.getInstance().getTrunkCode();
        if (phone.startsWith(countryCode)) {
            phone = trunkCode + phone.substring(countryCode.length());
        }

        if (!phone.startsWith(trunkCode)) {
            phone = trunkCode + phone;
        }

        return phone;
    }

    public static String getCurrencyCode(String code) {
        Map mapC = ApplicationConfig.getInstance().getMapCurrency();
        return (String)mapC.get(code);
    }

    public static String convertBalance(String balance) {
        if (balance == null) {
            return "0";
        } else {
            String result;
            String currency;
            try {
                String prefix = balance.substring(0, 4);
                if (prefix.contains("C")) {
                    result = "";
                } else {
                    result = "-";
                }

                currency = prefix.substring(0, 3);
            } catch (Exception var8) {
                return "0";
            }

            try {
                int lengthSize = balance.length();
                result = result + balance.substring(4, lengthSize);

                try {
                    String amount = formatStringToString(result);
                    String[] currencyArr = getCurrencyCode(currency).split(",");
                    Double amountTemp = formatStringISOToDouble(result);
                    if (!(amountTemp >= 1.0) && !amountTemp.equals(0.0)) {
                        if (currencyArr.length > 2) {
                            amount = amountTemp * 100.0 + " " + currencyArr[2];
                        } else {
                            amount = amountTemp * 100.0 + " cent";
                        }
                    } else if (currencyArr.length > 1) {
                        amount = amount + " " + currencyArr[1];
                    } else {
                        amount = "$ " + amount;
                    }

                    return amount;
                } catch (Exception var9) {
                    return "0";
                }
            } catch (Exception var10) {
                return result;
            }
        }
    }

    public static String convertBalanceNew(String balance, String[] symbol, int symbolPosition) {
        if (balance == null) {
            return "0";
        } else {
            String result;
            try {
                String prefix = balance.substring(0, 4);
                if (prefix.contains("C")) {
                    result = "";
                } else {
                    result = "-";
                }
            } catch (Exception var9) {
                return "0";
            }

            try {
                int lengthSize = balance.length();
                result = result + balance.substring(4, lengthSize);

                try {
                    String amount = formatStringToString(result);
                    Double amountTemp = formatStringISOToDouble(result);
                    if (!(amountTemp >= 1.0) && !amountTemp.equals(0.0)) {
                        amount = amountTemp * 100.0 + " " + symbol[1];
                    } else if (symbolPosition == 0) {
                        amount = symbol[0] + " " + amount;
                    } else {
                        amount = amount + " " + symbol[0];
                    }

                    return amount;
                } catch (Exception var10) {
                    return "0";
                }
            } catch (Exception var11) {
                return result;
            }
        }
    }

    public static String convertBalanceNoCurrency(String balance) {
        if (balance == null) {
            return "0";
        } else {
            String result;
            try {
                String prefix = balance.substring(0, 4);
                if (prefix.contains("C")) {
                    result = "";
                } else {
                    result = "-";
                }
            } catch (Exception var7) {
                return "0";
            }

            try {
                int lengthSize = balance.length();
                result = result + balance.substring(4, lengthSize);

                try {
                    String amount = formatStringToString(result);
                    return amount;
                } catch (Exception var5) {
                    return "0";
                }
            } catch (Exception var6) {
                return result;
            }
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        return StringUtils.leftPad(str, size, padStr);
    }

    public static boolean isPureAscii(String v) {
        return asciiEncoder.canEncode(v);
    }

    public static String convertAmount(Double amount) throws Exception {
        String amountStr;
        if (!(amount >= 1.0) && !(amount <= 0.0)) {
            amountStr = amount * 100.0 + " cent";
        } else {
            BigDecimal big = new BigDecimal(amount);
            amountStr = big.toString();
            if (amountStr.endsWith(".0")) {
                amountStr = amountStr.substring(0, amountStr.length() - 2);
            }

            amountStr = amountStr + "$";
        }

        return amountStr;
    }

    public static String convertAmountToCurrency(Double amount, String currency, String decimal) throws Exception {
        String amountStr;
        if (decimal != null && !decimal.isEmpty()) {
            if (!(amount >= 1.0) && amount != 0.0) {
                amountStr = String.format("%,.0f", amount * 100.0) + " " + decimal;
            } else if ("$".equals(currency)) {
                amountStr = "$" + String.format("%,.2f", amount);
            } else {
                amountStr = String.format("%,.2f", amount) + " " + currency;
            }
        } else {
            amountStr = String.format("%,.0f", amount) + " " + currency;
        }

        return amountStr;
    }

    public static String convertAmountUnitel(Double amount) throws Exception {
        BigDecimal big = new BigDecimal(amount);
        String amountStr = NumberFormat.getInstance().format(big);
        amountStr = amountStr + " LAK";
        return amountStr;
    }

    public static String show(String input) {
        return input;
    }

    public static String getCurrencyPosition(String language) {
        Map mapC = ApplicationConfig.getInstance().getMapPosition();
        return (String)mapC.get(language);
    }

    public static String formatCurrency(String input, String lang, String symbol, String currencyDecimal, int symbolPosition) {
        Double amountDouble = formatStringISOToDouble(input);
        if (amountDouble == null) {
            amountDouble = 0.0;
        }

        DecimalFormat decimalFormat = (DecimalFormat)DecimalFormat.getNumberInstance(LOCAL_EN);
        return decimalFormat.format(amountDouble) + " " + symbol;
    }

    public static String encodeBase64(String input) {
        if (input != null && !input.isEmpty()) {
            byte[] bytesEncoded = Base64.getEncoder().encode(input.getBytes());
            return new String(bytesEncoded);
        } else {
            return "";
        }
    }

    public static String decodeBase64(String input) {
        if (input != null && !input.isEmpty()) {
            String result = input;

            try {
                byte[] bytesEncoded = Base64.getDecoder().decode(input.getBytes());
                result = new String(bytesEncoded);
                if (result.contains("�")) {
                    return input;
                }
            } catch (Exception var3) {
            }

            return result;
        } else {
            return "";
        }
    }

    public static String encode(String input) {
        return StringEscapeUtils.escapeJava(input.trim());
    }

    public static String decode(String input) {
        return decodeBase64(StringEscapeUtils.unescapeJava(input.trim()));
    }

    public static String refineMobileNumberLao(String msisdn) {
        String countryCode = ApplicationConfig.getInstance().getCountryCode();
        switch (msisdn.length()) {
            case 8:
                return countryCode + "20" + msisdn;
            case 9:
                return countryCode + msisdn;
            case 10:
                if (String.valueOf(msisdn.charAt(0)).equals(ApplicationConfig.getInstance().getTrunkCode())) {
                    return countryCode + msisdn.substring(1, msisdn.length());
                }

                return countryCode + msisdn;
            case 11:
                return countryCode + msisdn.substring(1, msisdn.length());
            default:
                return msisdn;
        }
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getToken(int len) {
        char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] c = new char[len];
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < len; ++i) {
            c[i] = ch[random.nextInt(ch.length)];
        }

        return new String(c);
    }

    public static void showLineError(Exception e) {
        StackTraceElement[] elements = e.getStackTrace();

        for(int iterator = 1; iterator <= elements.length; ++iterator) {
        }

    }

    public static String convertHashMaptoString(HashMap<String, String> Hashmap) {
        String stringFromHashMap = "";
        if (Hashmap != null && !Hashmap.isEmpty()) {
            Iterator var2 = Hashmap.keySet().iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                String value = (String)Hashmap.get(key);
                if (value != null) {
                    System.out.println("key: " + key + " value: " + value);
                    stringFromHashMap = stringFromHashMap + key + "=" + value + "\r\n";
                }
            }

            return stringFromHashMap;
        } else {
            return "";
        }
    }

    public static String formatStringToSameAmount(Double input) {
        DecimalFormat decimalFormat = (DecimalFormat)DecimalFormat.getNumberInstance(LOCAL_EN);
        if (input == null) {
            return "0";
        } else {
            try {
                return decimalFormat.format(input);
            } catch (NumberFormatException var3) {
                return "0";
            }
        }
    }

    public static String getCurrencyDoubleMsg(String strDoubleAmount, String moneyCode) {
        String strCurrency = getCurrencyCode(moneyCode);
        String[] currencyArr = strCurrency.split(",");
        String currencySym = currencyArr.length > 2 ? currencyArr[1] : "";
        Double amountDouble = Double.parseDouble(strDoubleAmount);
        String strAmount = formatDoubleStringAmount(amountDouble);
        return strAmount + " " + currencySym;
    }

    public static HashMap<String, String> getHmResponse(String responseDescription) {
        try {
            if (responseDescription != null) {
                HashMap<String, String> hmResponse = null;
                Properties proAPP = new Properties();
                proAPP.load(new StringReader(responseDescription));
                Set set = proAPP.entrySet();
                Iterator itr = set.iterator();

                while(itr.hasNext()) {
                    if (hmResponse == null) {
                        hmResponse = new HashMap();
                    }

                    Map.Entry entry = (Map.Entry)itr.next();
                    hmResponse.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }

                return hmResponse;
            }
        } catch (IOException var6) {
        }

        return null;
    }

    public static boolean checkAgeLoto(Date birthDay) {
        try {
            Calendar calBirth = Calendar.getInstance();
            calBirth.setTime(birthDay);
            LocalDate targetBirth = LocalDate.of(calBirth.get(1), 2, 5);
            return Period.between(targetBirth, LocalDate.now()).getYears() < 18;
        } catch (Exception var3) {
            return true;
        }
    }

    public static String convertAmountWithoutCurrency(Double amount) {
        return String.format("%,.2f", amount);
    }

    static {
        LOCAL_GERMAN = Locale.GERMAN;
        LOCAL_EN = Locale.ENGLISH;
        asciiEncoder = Charset.forName("US-ASCII").newEncoder();
    }
}
