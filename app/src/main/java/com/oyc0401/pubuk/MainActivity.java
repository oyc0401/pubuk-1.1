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

    Date cu = Calendar.getInstance().getTime();
    SimpleDateFormat mfulldate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    int fulldate = Integer.parseInt(mfulldate.format(cu));

    public String JsonLunch,JsonTable;
    public int check=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //급식,시간표
        table_api tableApi= new table_api();
        tableApi.execute("3","10");
        lunch_api lunchApi=new lunch_api();
        lunchApi.execute(String.valueOf(fulldate));


        setNav();
        setpassword();

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
        Log.d(TAG, "onCreate13: "+JsonLunch);

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
    private void setpassword() {
        String SharedPrefFile = "com.example.android.SharedPreferences";
        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        //비번
        Dialog dl_login = new Dialog(MainActivity.this);// Dialog 초기화
        dl_login.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dl_login.setContentView(R.layout.dl_login);

        TextView tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_login.show();
            }
        });

        TextView tv_time1 = findViewById(R.id.tv_time1);
        EditText setID = dl_login.findViewById(R.id.setID);
        Button loginOK = dl_login.findViewById(R.id.loginOK);

        loginOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = setID.getText().toString();
                if (password.equals("happy")) {
                    login = 10;
                    tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_blue));
                    preferencesEditor.putInt("login", 10);
                    preferencesEditor.apply();
                    Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                } else if (password.equals("puzzle24")) {
                    login = 20;
                    tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_black));
                    preferencesEditor.putInt("login", 20);
                    preferencesEditor.apply();

                    Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                } else {
                    tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_white));
                    login = 0;
                    preferencesEditor.putInt("login", 0);
                    preferencesEditor.apply();
                }
                Log.d(TAG, password);
                dl_login.cancel();

            }
        });
        Log.d(TAG, "onCreate 로그인 코드: " + login);
        if (login == 0) {
            tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_white));
        } else if (login == 10) {
            tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_blue));
        } else if (login == 20) {
            tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_black));
        }
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


