package method;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDate {
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
    Calendar cal = Calendar.getInstance();

    public void setOperands(String strDate, int year, int month, int day) {
        try {
            Date dt = dtFormat.parse(strDate);
            cal.setTime(dt);
            cal.add(Calendar.YEAR, year);
            cal.add(Calendar.MONTH, month);
            cal.add(Calendar.DATE, day);
        } catch (Exception e) {

        }
    }

    public Integer get_date() {
        return Integer.valueOf(dtFormat.format(cal.getTime()));
    }

    public Integer get_day() {
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getCurMonday() {

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return formatter.format(c.getTime());
    }

    public static String getCurFriday() {

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        return formatter.format(c.getTime());
    }





}