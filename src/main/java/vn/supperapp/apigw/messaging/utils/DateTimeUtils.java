/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author truonglq
 */
public class DateTimeUtils {

    public static final String DDMMYYYY_FORMAT = "dd/MM/yyyy";
    public static final String DDMMYYYYHH_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static Date toDate(String date) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DDMMYYYY_FORMAT);
            return dateFormat.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static Date toDateTime(String date) throws Exception {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DDMMYYYYHH_FORMAT);
            return dateFormat.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Date getCurrentDateNoTime() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DDMMYYYY_FORMAT);
            Date today = new Date();
            Date todayWithZeroTime = formatter.parse(formatter.format(today));
            return todayWithZeroTime;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean currentDateInRange(Date startDate, Date endDate) {
        try {
            if (startDate == null || endDate == null) {
                return false;
            }

            Date currentDate = new Date();
            return startDate.compareTo(currentDate) <= 0 && currentDate.compareTo(endDate) <= 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static Date toDate(String date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Date toDate(Long timestamp) {
        try {
            return new Date(timestamp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String toString(Date value) {
        try {
            if (value != null) {
                SimpleDateFormat date = new SimpleDateFormat(DDMMYYYY_FORMAT);
                return date.format(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String toString(Date value, String format) {
        try {
            if (value != null) {
                SimpleDateFormat date = new SimpleDateFormat(format);
                return date.format(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String toString(Date value, String format, String timeZone) {
        try {
            if (value != null) {
                SimpleDateFormat date = new SimpleDateFormat(format);
                date.setTimeZone(TimeZone.getTimeZone(timeZone));
                return date.format(value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static Date addDate(Date date, int numOfDay) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE) + numOfDay,
                    0, // hour
                    0, // min
                    0); // sec

            //clear millisecond field
            calendar.clear(Calendar.MILLISECOND);
            return calendar.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String addDate(String strDate, int numOfDay) {
        try {
            Date date = toDate(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE) + numOfDay,
                    0, // hour
                    0, // min
                    0); // sec

            //clear millisecond field
            calendar.clear(Calendar.MILLISECOND);
            return toString(calendar.getTime(), "ddMMMyyyy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String addMinute(Date expDate, int numOfMinutes) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expDate);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE),
                    calendar.get(Calendar.HOUR), // hour
                    calendar.get(Calendar.MINUTE) + numOfMinutes, // min
                    calendar.get(Calendar.SECOND)); // sec

            //clear millisecond field
            calendar.clear(Calendar.MILLISECOND);
            return toString(calendar.getTime(), "dd-MMM-yyyy HH:mm:ss");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static Date addHours(Date expDate, int numOfHours) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expDate);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE),
                    calendar.get(Calendar.HOUR) + numOfHours, // hour
                    calendar.get(Calendar.MINUTE), // min
                    calendar.get(Calendar.SECOND)); // sec

            //clear millisecond field
            calendar.clear(Calendar.MILLISECOND);
            return calendar.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Integer numberOfDaysBetween(Date date1, Date date2) {
        try {
            Calendar cal1 = new GregorianCalendar();
            Calendar cal2 = new GregorianCalendar();
            cal1.setTime(date1);
            cal2.setTime(date2);

            int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
            return days;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static long diffBetweenDates(Date fromDate, Date toDate, TimeUnit unit) {
        if (fromDate == null || toDate == null) {
            return 0;
        }
        long diff = toDate.getTime() - fromDate.getTime();
        if (TimeUnit.DAYS.compareTo(unit) == 0) {
            return (long) diff / (1000 * 60 * 60 * 24);
        } else if (TimeUnit.HOURS.compareTo(unit) == 0) {
            return diff / (60 * 60 * 1000);
        } else if (TimeUnit.MINUTES.compareTo(unit) == 0) {
            return diff / (60 * 1000) % 60;
        } else if (TimeUnit.SECONDS.compareTo(unit) == 0) {
            return diff / 1000 % 60;
        }
        return 0;
    }

    public static Date getddMMMyy(Date date, String pattern) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return convertStringToDate(formatter.format(date), pattern);
    }

    public static Date convertStringToDate(String inputDate, String pattern) throws Exception {
        Date result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            if (inputDate != null) {
                dateFormat.setLenient(false);
                result = dateFormat.parse(inputDate);
            }
        } catch (Exception e) {
            throw new Exception();
        }
        return result;
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static String convertDateToString(Date date, String format) {
        String dateStr = null;
        DateFormat df = new SimpleDateFormat(format);

        try {
            dateStr = df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateStr;
    }

    public static String convertDateFormat(String str) {

        if (CommonUtils.isNullOrEmpty(str)) {
            return null;
        }
        return str.replace("/", "");
    }

    public static int getAge(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        int age = 0;
        try {
            Calendar today = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(dateOfBirth);
            if (birthDate.after(today)) {
                return 0;
            }
            int todayYear = today.get(Calendar.YEAR);
            int birthDateYear = birthDate.get(Calendar.YEAR);
            int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
            int birthDateDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR);
            int todayMonth = today.get(Calendar.MONTH);
            int birthDateMonth = birthDate.get(Calendar.MONTH);
            int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
            int birthDateDayOfMonth = birthDate.get(Calendar.DAY_OF_MONTH);
            age = todayYear - birthDateYear;

            // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
            if ((birthDateDayOfYear - todayDayOfYear > 3) || (birthDateMonth > todayMonth)) {
                age--;

                // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
            } else if ((birthDateMonth == todayMonth) && (birthDateDayOfMonth > todayDayOfMonth)) {
                age--;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return age;
    }

    public static Integer getTimeUnit(Date date, int timeUnit) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(timeUnit);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        if (true) {
            System.out.println(toString(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "UCT"));
            return;
        }
        if (true) {
            Date d = toDate("2020-06-26T14:36:49.485872", "yyyy-MM-dd'T'HH:mm:ss");
            System.out.println(toString(d, "dd/MM/yyyy HH:mm:ss.SSS"));
            return;
        }
        if (true) {
            System.out.println(toString(addDate(new Date(), 60), "MMMM, yyyy"));
            return;
        }
        System.out.println(DateTimeUtils.toString(new Date(), "dd/MM/yyyy HH:mm:ss"));

    }
}

