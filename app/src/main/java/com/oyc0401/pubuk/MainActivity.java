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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.oyc0401.pubuk.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Observer;

import method.AddDate;
import method.parse;


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
    SimpleDateFormat mdate = new SimpleDateFormat("dd", Locale.getDefault());
    SimpleDateFormat mmonth = new SimpleDateFormat("MM", Locale.getDefault());
    int fulldate = Integer.parseInt(mfulldate.format(cu));
    int date = Integer.parseInt(mdate.format(cu));
    int month = Integer.parseInt(mmonth.format(cu));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //뷰 바인딩
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //SharedPreferences 추가
        String SharedPrefFile = "com.example.android.SharedPreferences";
        SharedPreferences mPreferences = this.getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        //뷰모델 추가
        ViewModel model = new ViewModelProvider(MainActivity.this).get(ViewModel.class);

        //파이어베이스 추가
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

































        oncreate(mPreferences, preferencesEditor, model, storageRef);

    }

    private void oncreate(SharedPreferences mPreferences, SharedPreferences.Editor preferencesEditor, ViewModel model, StorageReference storageRef) {
        //급식사진 uri 뷰모델에 저장
        String lunch_filename = "lunch_menu/" + fulldate + ".jpg";
        StorageReference subp = storageRef.child(lunch_filename);
        subp.getDownloadUrl().addOnSuccessListener(uri -> model.img_lunch.setValue(uri));

        //배너사진 uri 뷰모델에 저장
        String banner_filename = "banner/" + 1 + ".jpg";
        StorageReference subp_banner = storageRef.child(banner_filename);
        subp_banner.getDownloadUrl().addOnSuccessListener(uri -> model.img_banner.setValue(uri));

        //대회사진1 uri 뷰모델에 저장
        String congress_filename1 = "congress/" + 1 + ".jpg";
        StorageReference subp_congress1 = storageRef.child(congress_filename1);
        subp_congress1.getDownloadUrl().addOnSuccessListener(uri -> model.img_con1.setValue(uri));

        //대회사진2 uri 뷰모델에 저장
        String congress_filename2 = "congress/" + 2 + ".jpg";
        StorageReference subp_congress2 = storageRef.child(congress_filename2);
        subp_congress2.getDownloadUrl().addOnSuccessListener(uri -> model.img_con2.setValue(uri));

        //대회사진3 uri 뷰모델에 저장
        String congress_filename3 = "congress/" + 3 + ".jpg";
        StorageReference subp_congress3 = storageRef.child(congress_filename3);
        subp_congress3.getDownloadUrl().addOnSuccessListener(uri -> model.img_con3.setValue(uri));

        ///대회사진4 uri 뷰모델에 저장
        String congress_filename4 = "congress/" + 4 + ".jpg";
        StorageReference subp_congress4 = storageRef.child(congress_filename4);
        subp_congress4.getDownloadUrl().addOnSuccessListener(uri -> model.img_con4.setValue(uri));

        //급식,시간표
        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);

        table_api tableApi = new table_api();
        tableApi.execute(String.valueOf(grade), String.valueOf(clas));

        lunch_api lunchApi = new lunch_api();
        lunchApi.execute(String.valueOf(fulldate));


        setNav();
        setpassword(mPreferences, preferencesEditor, model);
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


    private void setNav() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                Log.d(TAG, "onCreate:지금 어디 " + Objects.requireNonNull(navController.getCurrentDestination()).toString()));

        BottomNavigationView navView = findViewById(R.id.nav_view);


        navView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!Objects.requireNonNull(navController.getCurrentDestination()).toString().equals("Destination(com.oyc0401.pubuk:id/navigation_home) label=Home class=com.oyc0401.pubuk.ui.HomeFragment")) {
                        navController.navigate(R.id.navigation_home);
                        Log.d(TAG, "setNav: dsad");
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (!Objects.requireNonNull(navController.getCurrentDestination()).toString().equals("Destination(com.oyc0401.pubuk:id/navigation_dashboard) label=Dashboard class=com.oyc0401.pubuk.ui.DashboardFragment")) {
                        Intent intent = new Intent(MainActivity.this, ScrollingActivity111.class);
                        startActivity(intent);
                        navController.navigate(R.id.navigation_dashboard);
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (!Objects.requireNonNull(navController.getCurrentDestination()).toString().equals("Destination(com.oyc0401.pubuk:id/navigation_notifications) label=Notifications class=com.oyc0401.pubuk.ui.NotificationsFragment")) {
                        navController.navigate(R.id.navigation_notifications);
                    }
                    return true;
            }
            return false;
        });

        //NavigationUI.setupWithNavController(binding.navView, navController); 이거 하면 무조건 디스트로이됌

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

        model.login.setValue(mPreferences.getInt("login", 0));

        loginOK.setOnClickListener(v -> {
            String password = setID.getText().toString();

            if (password.equals("happy")) {
                model.login.setValue(10);
                preferencesEditor.putInt("login", 10);
                Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
            } else if (password.equals("puzzle24")) {
                model.login.setValue(20);
                preferencesEditor.putInt("login", 20);
                Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
            } else {
                model.login.setValue(0);
                preferencesEditor.putInt("login", 0);
            }
            preferencesEditor.apply();
            Log.d(TAG, password);
            dl_login.cancel();

        });
        Log.d(TAG, "onCreate 로그인 코드: " + login);
    }

    public class table_api extends AsyncTask<String, Void, String> {
        private String receiveMsg;
        private String[][] arr = new String[10][8];

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
            arr = Array_table(receiveMsg);
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ViewModel model = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
            model.arr_table.setValue(arr);
        }


        private String[][] Array_table(String json) {
            String[][] arr = new String[10][8];

            for (int i = 0; i <= 7; i++) {//배열 초기화
                for (int j = 0; j <= 7; j++) {
                    arr[i][j] = " ";//arr[가로줄][교시]
                }
            }
            try {//json 문자열을 배열에 넣음
                JSONArray jarray1 = new JSONObject(json).getJSONArray("hisTimetable");
                JSONObject jobject1 = jarray1.getJSONObject(1);
                JSONArray jarray2 = jobject1.getJSONArray("row");
                for (int i = 0; i <= 35; i++) {
                    try {
                        JSONObject jobject2 = jarray2.getJSONObject(i);
                        String ALL_TI_YMD = jobject2.optString("ALL_TI_YMD");
                        String PERIO = jobject2.optString("PERIO");
                        String ITRT_CNTNT = jobject2.optString("ITRT_CNTNT");
                        int k = 0;
                        int a = Integer.parseInt(ALL_TI_YMD);
                        int b = Integer.parseInt(AddDate.getCurFriday());
                        AddDate add = new AddDate();
                        for (int t = 0; a != b; t++) {
                            add.setOperands(ALL_TI_YMD, 0, 0, t);
                            a = add.get_date();
                            k = t;
                        }
                        int difference = 5 - k;
                        arr[Integer.parseInt(PERIO)][difference] = ITRT_CNTNT;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return arr;
        }
    }

    public class lunch_api extends AsyncTask<String, Void, String> {//급식 json 파일을 Shared에 저장하고 get table,set talbe실행
        private String receiveMsg;
        private String[][] arr = new String[14][40];

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
            arr = Array_lunch(receiveMsg);
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ViewModel model = new ViewModelProvider(MainActivity.this).get(ViewModel.class);
            model.arr_lunch.setValue(arr);
        }

        private String[][] Array_lunch(String json) {

            String[][] arraysum = new String[35][3];
            String[][] arr = new String[14][40];

            int how_much_parsing = 25;

            //배열 초기화
            for (int i = 0; i <= 30; i++) {
                arraysum[i][0] = "0";
                arraysum[i][1] = "0";
            }

            try {//json 문자열을 배열에 넣음
                JSONArray jarray1 = new JSONObject(json).getJSONArray("mealServiceDietInfo");
                JSONObject jobject1 = jarray1.getJSONObject(1);
                JSONArray jarray2 = jobject1.getJSONArray("row");

                for (int i = 0; i <= how_much_parsing; i++) {
                    JSONObject jobject2 = jarray2.getJSONObject(i);
                    String MLSV_YMD = jobject2.optString("MLSV_YMD");
                    String DDISH_NM = jobject2.optString("DDISH_NM");
                    DDISH_NM = DDISH_NM.replace("<br/>", "\n");//문자열 바꾸기
                    DDISH_NM = DDISH_NM.replace("-북고", "");//문자열 바꾸기
                    DDISH_NM = DDISH_NM.replace(".", "");//문자열 바꾸기

                    for (int j = 0; j <= 20; j++) {
                        DDISH_NM = DDISH_NM.replace(String.valueOf(j), "");//알레르기 문자열 바꾸기
                    }
                    arraysum[i][0] = MLSV_YMD;
                    arraysum[i][1] = DDISH_NM;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //arr배열 초기화 (앞으로 2달만 설정)
            for (int i = 0; i <= 31; i++) {
                for (int j = month; j <= month + 2; j++) {
                    arr[j][i] = "급식 정보가 없습니다.";
                }
            }

            for (int i = 0; i <= how_much_parsing; i++) {//배열에 담긴 점심을 분류해서 lunch에 옮김
                int oridate = Integer.parseInt(arraysum[i][0]);
                if (oridate != 0) {
                    int lunch_month = ((oridate - 20210000) / 100);
                    int lunch_date = (oridate - 20210000 - lunch_month * 100);
                    arr[lunch_month][lunch_date] = arraysum[i][1];

                }
            }
            return arr;
        }
    }

}


