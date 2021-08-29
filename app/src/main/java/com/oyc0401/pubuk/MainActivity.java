package com.oyc0401.pubuk;

import static com.oyc0401.pubuk.R.drawable.time1_black;
import static com.oyc0401.pubuk.R.drawable.time1_blue;
import static com.oyc0401.pubuk.R.drawable.time1_white;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.app.SharedElementCallback;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oyc0401.pubuk.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import method.AddDate;
import method.parse;
import method.storage;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    int login = 0;
    String TAG = "로그";
    long backKeyPressedTime = 0;
    Toast toast;
    int grade, clas, width, height, LunchTextView_Width, first_lunch_view, Setting_To_Main;
    int check_oncreate = 0;


    Date cu = Calendar.getInstance().getTime();
    SimpleDateFormat mfulldate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    SimpleDateFormat mrealfulldate = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss,SSSS", Locale.getDefault());
    SimpleDateFormat mdate = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat mmonth = new SimpleDateFormat("MM", Locale.getDefault());
    int fulldate = Integer.parseInt(mfulldate.format(cu));
    String realfulldate = mrealfulldate.format(cu);
    int date = Integer.parseInt(mdate.format(cu));
    int month = Integer.parseInt(mmonth.format(cu));




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String SharedPrefFile = "com.example.android.SharedPreferences";
        SharedPreferences mPreferences = this.getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        ViewModel model = new ViewModelProvider(MainActivity.this).get(ViewModel.class);


        //급식,시간표
        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);
        table_api tableApi= new table_api();
        tableApi.execute(String.valueOf(grade) ,String.valueOf(clas));
        lunch_api lunchApi=new lunch_api();
        lunchApi.execute(String.valueOf(fulldate));


        setNav();




        setpassword(mPreferences, preferencesEditor, model);

    }

    private void setpassword(SharedPreferences mPreferences, SharedPreferences.Editor preferencesEditor, ViewModel model) {
        //비번
        Dialog dl_login = new Dialog(MainActivity.this);// Dialog 초기화
        dl_login.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dl_login.setContentView(R.layout.dl_login);
        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setOnClickListener(v -> dl_login.show());

        EditText setID = dl_login.findViewById(R.id.setID);
        Button loginOK = dl_login.findViewById(R.id.loginOK);

        model.setIntLogin(mPreferences.getInt("login",0));

        loginOK.setOnClickListener(v -> {
            String password = setID.getText().toString();

            if (password.equals("happy")) {
                model.setIntLogin(10);
                preferencesEditor.putInt("login",10);
                Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
            } else if (password.equals("puzzle24")) {
                model.setIntLogin(20);
                preferencesEditor.putInt("login",20);
                Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
            } else {
                model.setIntLogin(0);
                preferencesEditor.putInt("login",0);
            }
            preferencesEditor.apply();
            Log.d(TAG, password);
            dl_login.cancel();

        });
        Log.d(TAG, "onCreate 로그인 코드: " + login);
    }


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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ViewModel model = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
            model.setTextTable(receiveMsg);

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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ViewModel model = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
            model.setTextLunch(receiveMsg);

        }
    }



    private void setNav() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.d(TAG, "onCreate:지금 어디 " + navController.getCurrentDestination().toString());
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d(TAG, "onNavigationItemSelected: 111");
                    if (navController.getCurrentDestination().toString().equals("Destination(com.oyc0401.pubuk:id/navigation_home) label=Home class=com.oyc0401.pubuk.ui.home.HomeFragment")) {
                    } else {
                        navController.navigate(R.id.navigation_home);
                    }


                    return true;
                case R.id.navigation_dashboard:
                    Log.d(TAG, "onNavigationItemSelected: 222");
                    if (navController.getCurrentDestination().toString().equals("Destination(com.oyc0401.pubuk:id/navigation_dashboard) label=Dashboard class=com.oyc0401.pubuk.ui.dashboard.DashboardFragment")) {
                    } else {
                        navController.navigate(R.id.navigation_dashboard);
                    }


                    return true;
                case R.id.navigation_notifications:
                    Log.d(TAG, "onNavigationItemSelected: 333");
                    if (navController.getCurrentDestination().toString().equals("Destination(com.oyc0401.pubuk:id/navigation_notifications) label=Notifications class=com.oyc0401.pubuk.ui.notifications.NotificationsFragment")) {
                    } else {
                        navController.navigate(R.id.navigation_notifications);
                    }


                    return true;
            }
            return false;
        });
    }



    @Override
    public void onBackPressed() { //뒤로가기 버튼 2초안에 한번 더 누르면 종료

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

}


