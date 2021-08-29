package method;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.oyc0401.pubuk.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import method.AddDate;
import method.parse;

public class storage extends AppCompatActivity {
    int grade, clas, width, height, LunchTextView_Width, login, first_lunch_view, Setting_To_Main;

    Date cu = Calendar.getInstance().getTime();
    SimpleDateFormat mfulldate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    SimpleDateFormat mrealfulldate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss,SSSS", Locale.getDefault());
    SimpleDateFormat mdate = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat mmonth = new SimpleDateFormat("MM", Locale.getDefault());
    int fulldate = Integer.parseInt(mfulldate.format(cu));
    String realfulldate = mrealfulldate.format(cu);
    int date = Integer.parseInt(mdate.format(cu));
    int month = Integer.parseInt(mmonth.format(cu));
    String SharedPrefFile = "com.example.android.SharedPreferences";

    String json_lunch,json_table;



    private void start() throws ExecutionException, InterruptedException {

        table_api tableApi=new table_api();
        lunch_api lunchApi=new lunch_api();
        this.json_table=tableApi.execute("3","10").get();
        this.json_lunch=lunchApi.execute(String.valueOf(fulldate)).get();
    }

    public String getJson_lunch() throws ExecutionException, InterruptedException {
        start();
        return json_lunch;
    }
    public String getJson_table() throws ExecutionException, InterruptedException {
        start();
        return json_table;
    }



    public String getJson_table11(){
        String param1 ;
        String param2 ;
        param1="3";
        param2="10";
        String fir = AddDate.getCurMonday();
        String las = AddDate.getCurFriday();
        String receiveMsg = parse.json("https://open.neis.go.kr/hub/hisTimetable?Key=59b8af7c4312435989470cba41e5c7a6&Type=json&pIndex=1&pSize=1000&ATPT_OFCDC_SC_CODE=J10&SD_SCHUL_CODE=7530072&GRADE=" + param1 + "&CLASS_NM=" + param2 + "&TI_FROM_YMD=" + fir + "&TI_TO_YMD=" + las);
        return receiveMsg;

    }

    private static final String TAG = "로그";

    public class table_api extends AsyncTask<String, Void, String> {
        private String receiveMsg;
        protected void onPreExecute() {
            Log.d("로그", "table_api 시작");
        }
        @Override
        protected String doInBackground(String... params) {
            String param1 = params[0];
            String param2 = params[1];
            String fir = AddDate.getCurMonday();
            String las = AddDate.getCurFriday();
            receiveMsg = parse.json("https://open.neis.go.kr/hub/hisTimetable?Key=59b8af7c4312435989470cba41e5c7a6&Type=json&pIndex=1&pSize=1000&ATPT_OFCDC_SC_CODE=J10&SD_SCHUL_CODE=7530072&GRADE=" + param1 + "&CLASS_NM=" + param2 + "&TI_FROM_YMD=" + fir + "&TI_TO_YMD=" + las);
            return receiveMsg;
        }
    }

    public class lunch_api extends AsyncTask<String, Void, String> {//급식 json 파일을 Shared에 저장하고 get table,set talbe실행
        private String receiveMsg;
        protected void onPreExecute() {
            Log.d("로그", "lunch_api 시작");
        }
        @Override
        protected String doInBackground(String... params) {
            String date1 = params[0];
            AddDate add = new AddDate();
            add.setOperands(date1, 0, 0, 30);
            int date2 = add.get_date();
            receiveMsg = parse.json("https://open.neis.go.kr/hub/mealServiceDietInfo?Key=59b8af7c4312435989470cba41e5c7a6&Type=json&pIndex=1&pSize=1000&ATPT_OFCDC_SC_CODE=J10&SD_SCHUL_CODE=7530072&MLSV_FROM_YMD=" + date1 + "&MLSV_TO_YMD=" + date2);
            return receiveMsg;
        }
    }

}
