package method;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDate {
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
    Calendar cal = Calendar.getInstance();

    //추가할 날짜 설정 (안해도 됌)
    public void setOperands(String strDate, int year, int month, int date) {
        try {
            Date dt = dtFormat.parse(strDate);
            cal.setTime(dt);
            cal.add(Calendar.YEAR, year);
            cal.add(Calendar.MONTH, month);
            cal.add(Calendar.DATE, date);
        } catch (Exception e) {

        }
    }

    //추가된 날짜의 일 구하기
    public Integer get_date() {
        return Integer.valueOf(dtFormat.format(cal.getTime()));
    }

    //추가된 날짜의 요일 구하기
    public Integer get_day() {
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    //추가된 날짜의 요일 구하기
    //1=일요일, 2=화요일, 7=토요일
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