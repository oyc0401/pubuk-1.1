package com.oyc0401.pubuk;

import static com.oyc0401.pubuk.R.drawable.time1_black;
import static com.oyc0401.pubuk.R.drawable.time1_blue;
import static com.oyc0401.pubuk.R.drawable.time1_white;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

import method.AddDate;
import method.parse;

public class MainActivity extends AppCompatActivity {
    int grade, clas, width, height, LunchTextView_Width, login, first_lunch_view, Setting_To_Main;
    int check_oncreate = 0;
    private long backKeyPressedTime = 0;
    private Toast toast;

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
    String dada;
    String TAG = "로그";

    String[][] lunch = new String[14][40];
    String[][] timetable_item = new String[10][8];
    String[][] timetable_original = new String[10][8];
    String[] day = {"", "일", "월", "화", "수", "목", "금", "토"};


    TextView tv[][] = new TextView[10][10];
    ImageView iv_image, iv_banner, iv_congress, iv_congress2, iv_congress3, iv_congress4;//이미지
    int REQUEST_CODE = 0;

    Dialog dl_login; // 커스텀 다이얼로그



//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check_oncreate = 1;
        iv_image = findViewById(R.id.iv_image);//이미지
        iv_banner = findViewById(R.id.iv_banner);
        iv_congress = findViewById(R.id.iv_congress1);
        iv_congress2 = findViewById(R.id.iv_congress2);
        iv_congress3 = findViewById(R.id.iv_congress3);
        iv_congress4 = findViewById(R.id.iv_congress4);
        //new DownloadFilesTask().execute("http://www.puchonbuk.hs.kr/upload/l_passquery/20210601_2.jpeg");//이미지

        Intent intent_univ = new Intent(this, ScrollingActivity111.class);//대학입결 인텐트
        Intent intent_setting = new Intent(this, setting.class);//설정 인텐트
        Button btn_univ = findViewById(R.id.btn_univ);
        TextView lunch1 = findViewById(R.id.lunch);
        HorizontalScrollView hsv = findViewById(R.id.hsv);
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        LinearLayout lunchview = findViewById(R.id.lunchview);

        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);
        preferencesEditor.putInt("setting_To_Main", 1);
        login = mPreferences.getInt("login", 0);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size); // 화면사이즈 구하기
        width = size.x; // 너비
        height = size.y; // 높이구하기
        LunchTextView_Width = width * 3 / 8; // 급식표 사이즈 비율
////////////////////////////////////////////////////////////////////////////////////////////




//라이츄

//피카츄

        TextView tv_timetable321=findViewById(R.id.tv_timetable321);
        tv_timetable321.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= 7; i++) {
                    for (int j = 1; j <= 5; j++) {
                        tv[i][j].setText(timetable_original[i][j]);
                        tv[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }
        });







        ///학급 일정
        set_perform();
        TextView tv_perform = findViewById(R.id.tv_perform_title);
        tv_perform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(grade + "_" + clas);

                EditText et_perfomance = findViewById(R.id.et_performance);
                myRef.setValue(et_perfomance.getText().toString());
                Toast.makeText(MainActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        //////////////급식사진
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String lunch_filename = String.valueOf(fulldate);
        StorageReference subp = storageRef.child("lunch_menu/" + lunch_filename + ".jpg");
        subp.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).into(iv_image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        //둥근 이미지
        iv_image.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, iv_image.getWidth(), iv_image.getHeight(), 30);
            }
        });
        iv_image.setClipToOutline(true);

        TextView tv_lunchPicture_title = findViewById(R.id.tv_lunchPicture_title);
        tv_lunchPicture_title.setText(month+"월 "+date+"일 급식사진");
        tv_lunchPicture_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login == 10|login==20) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    REQUEST_CODE = 1;
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //Toast.makeText(MainActivity.this, "권한이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //////배너 사진

        StorageReference subp_banner = storageRef.child("banner/" + 1 + ".jpg");
        subp_banner.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                Glide.with(MainActivity.this).load(uri1).into(iv_banner);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        ImageView iv_banner = findViewById(R.id.iv_banner);
        TextView memo=findViewById(R.id.memo);
        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login == 20) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    REQUEST_CODE = 2;
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //Toast.makeText(MainActivity.this, "권한이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("banner_url");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        String value = dataSnapshot.getValue(String.class);
                        //Log.d("로그", "Value is: " + value);
                        intent.setData(Uri.parse(value));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("로그", "Failed to read value.", error.toException());
                    }
                });


            }
        });


        ////////대회사진
        StorageReference subp_congress = storageRef.child("congress/" + 1 + ".jpg");
        subp_congress.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                Glide.with(MainActivity.this).load(uri1).into(iv_congress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        StorageReference subp_congress2 = storageRef.child("congress/" + 2 + ".jpg");
        subp_congress2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                Glide.with(MainActivity.this).load(uri1).into(iv_congress2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        StorageReference subp_congress3 = storageRef.child("congress/" + 3 + ".jpg");
        subp_congress3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                Glide.with(MainActivity.this).load(uri1).into(iv_congress3);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        StorageReference subp_congress4 = storageRef.child("congress/" + 4 + ".jpg");
        subp_congress4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                Glide.with(MainActivity.this).load(uri1).into(iv_congress4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });





        TextView tv_congress_title = findViewById(R.id.tv_congress_title);
        tv_congress_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login == 20) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    REQUEST_CODE = 3;
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //Toast.makeText(MainActivity.this, "권한이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 시간표 findViewById 하기
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                tv[i][j] = findViewById(getResources().getIdentifier("tt" + (i) + "_" + (j), "id", this.getPackageName()));
            }
        }

        //처음 실핼할때만 설정화면 이동하기
        int check = mPreferences.getInt("check", 0);
        if (check == 0) {
            preferencesEditor.putInt("check", 1);
            preferencesEditor.apply();
            startActivity(intent_setting);
        }

        //기본 시간표
        parse par = new parse();
        par.setgrade(grade, clas);
        timetable_original = Array_tableOriginal(grade, clas);
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                tv[i][j].setText(timetable_original[i][j]);

            }
        }

        //급식함수 실행
        lunch_api lunchApi = new lunch_api();
        lunchApi.execute(String.valueOf(fulldate));

        //시간표 함수 실행
        table_api tableApi = new table_api();
        tableApi.execute(String.valueOf(grade), String.valueOf(clas));

        //네비게이션바 설정
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {//바텀 네비게이션 설정
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        return true;
                    case R.id.navigation_dashboard:
                        startActivity(intent_univ);
                        //Toast.makeText(MainActivity.this, "미구현", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_notifications:
                        startActivity(intent_setting);
                        return true;
                }
                return false;
            }
        });

        //입결 버튼 설정
        btn_univ.setOnClickListener(new View.OnClickListener() {//대학입결 버튼
            @Override
            public void onClick(View v) {
                startActivity(intent_univ);
                overridePendingTransition(0, 0);
            }
        });


        //비번
        dl_login= new Dialog(MainActivity.this);// Dialog 초기화
        dl_login.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dl_login.setContentView(R.layout.dl_login);

        TextView tv_toolbar=findViewById(R.id.tv_toolbar);
        tv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_login.show();
            }
        });

        TextView tv_time1=findViewById(R.id.tv_time1);
        EditText setID = dl_login.findViewById(R.id.setID);
        Button loginOK = dl_login.findViewById(R.id.loginOK);
        loginOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password= setID.getText().toString();
                if(password.equals("happy")){
                    login=10;
                    tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_blue));
                    preferencesEditor.putInt("login", 10);
                    preferencesEditor.apply();
                    Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                }else if(password.equals("puzzle24")){
                    login=20;
                    tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_black));
                    preferencesEditor.putInt("login", 20);
                    preferencesEditor.apply();

                    Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                }else{
                    tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_white));
                    login=0;
                    preferencesEditor.putInt("login", 0);
                    preferencesEditor.apply();
                }
                Log.d(TAG, password);
                dl_login.cancel();

            }
        });
        Log.d(TAG, "onCreate 로그인 코드: "+login);
        if (login==0){
            tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_white));
        }else if (login==10){
            tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_blue));
        }else if (login==20){
            tv_time1.setBackground(ContextCompat.getDrawable(MainActivity.this, time1_black));
        }


    }


    private String[][] Array_table() {
        String[][] arr = new String[10][8];

        for (int i = 0; i <= 7; i++) {//배열 초기화
            for (int j = 0; j <= 7; j++) {
                arr[i][j] = " ";//arr[가로줄][교시]
            }
        }

        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        String tablese = mPreferences.getString("table_json", "");

        try {//json 문자열을 배열에 넣음
            JSONArray jarray1 = new JSONObject(tablese).getJSONArray("hisTimetable");
            JSONObject jobject1 = jarray1.getJSONObject(1);
            JSONArray jarray2 = jobject1.getJSONArray("row");

            for (int i = 0; i <= 35; i++) {
                try {
                    JSONObject jobject2 = jarray2.getJSONObject(i);
                    String ALL_TI_YMD = jobject2.optString("ALL_TI_YMD");
                    String PERIO = jobject2.optString("PERIO");
                    String ITRT_CNTNT = jobject2.optString("ITRT_CNTNT");
                    int k = 0, difference = 0;
                    int a = Integer.parseInt(ALL_TI_YMD);
                    int b = Integer.parseInt(AddDate.getCurFriday());
                    AddDate add = new AddDate();
                    for (int t = 0; a != b; t++) {
                        add.setOperands(ALL_TI_YMD, 0, 0, t);
                        a = add.get_date();
                        k = t;
                    }
                    difference = 5 - k;
                    arr[Integer.parseInt(PERIO)][difference] = ITRT_CNTNT;
                } catch (Exception e) {

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    private String[][] Array_lunch() {
        String[][] arraysum = new String[35][3];
        String[][] arr = new String[14][40];

        int 몇일을파싱 = 25;
        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        String lunch_json = mPreferences.getString("lunch_json", "");
        for (int i = 0; i <= 30; i++) {//배열 초기화
            arraysum[i][0] = "0";
            arraysum[i][1] = "0";
        }

        try {//json 문자열을 배열에 넣음
            JSONArray jarray1 = new JSONObject(lunch_json).getJSONArray("mealServiceDietInfo");
            JSONObject jobject1 = jarray1.getJSONObject(1);
            JSONArray jarray2 = jobject1.getJSONArray("row");

            for (int i = 0; i <= 몇일을파싱; i++) {
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

        for (int i = 0; i <= 몇일을파싱; i++) {//배열에 담긴 점심을 분류해서 lunch에 옮김
            int oridate = Integer.parseInt(arraysum[i][0]);
            if (oridate != 0) {
                int lunch_month = ((oridate - 20210000) / 100);
                int lunch_date = (oridate - 20210000 - lunch_month * 100);
                arr[lunch_month][lunch_date] = arraysum[i][1];

            }
        }
        return arr;
    }

    private String[][] Array_tableOriginal(int grade, int clas) {
        String[][] arr = new String[10][8];
        for (int i = 0; i <= 6; i++) {//배열 초기화
            for (int j = 0; j <= 7; j++) {
                arr[i][j] = " ";
            }
        }

        AssetManager assetManager = getAssets();
        try {
            InputStream is = assetManager.open("lunch/table_original.json");//"lunch/table_original.json",,,,"lunch/table_original_backup.json"

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }
            String jsonData = buffer.toString();

            JSONArray jarray1 = new JSONObject(jsonData).getJSONArray("timetable");

            int table_clas_num = 1;
            if (grade == 1) {
                table_clas_num = clas - 1;
            } else if (grade == 2) {
                table_clas_num = clas + 9 - 1;
            } else if (grade == 3) {
                table_clas_num = clas + 18 - 1;
            }
            //Log.d("로그-jjj", String.valueOf(table_clas_num));
            JSONObject jobject1 = jarray1.getJSONObject(table_clas_num);


            for (int i = 1; i <= 7; i++) {
                JSONArray jarray2 = jobject1.getJSONArray(i + "교시");
                //Log.d("로그-jjj", String.valueOf(jarray2));
                for (int j = 0; j <= 4; j++) {
                    //Log.d("로그 d3ed","i는"+i+" j는"+j+" "+jarray2.optString(j));
                    arr[i][j + 1] = jarray2.optString(j);

                }
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }


        return arr;
    }


    private void set_perform() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(grade + "_" + clas);

        TextView tv_perform = findViewById(R.id.tv_perform_title);
        tv_perform.setText(grade + "학년 " + clas + "반 일정");
        EditText et_perfomance = findViewById(R.id.et_performance);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d("로그", "Value is: " + value);
                et_perfomance.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("로그", "Failed to read value.", error.toException());
            }
        });
    }

    private void set_table() {//저장된 json파일을 시간표배열에 넣고 시간표 보여줌
        timetable_item = Array_table();

        String a = timetable_item[1][1];
        String b = timetable_item[1][2];
        String c = timetable_item[1][3];
        String d = timetable_item[1][4];
        String e = timetable_item[1][5];


        if ((a != " ") | (b != " ") | (c != " ") | (d != " ") | (e != " ")) {
            //Log.d("로그-성공!", a);
            for (int i = 1; i <= 7; i++) {//시간표 설정
                for (int j = 1; j <= 5; j++) {
                    tv[i][j].setText(timetable_item[i][j]);
                    tv[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    if (timetable_item[i][j].equals(timetable_original[i][j]) == false) {
                        tv[i][j].setBackgroundColor(Color.parseColor("#FFFDE7"));
                    }
                    //Log.d("오", "ㅇ");

                }
            }
        } else {
            //Log.d("로그-시류패ㅠㅠ", a);
        }


    }

    private void set_lunch() {//저장된 json파일을 급식배열에 넣고 급식 보여줌

        int date_number = 30;
        lunch = Array_lunch();
        first_lunch_view = 10;
        for (int i = 0; i <= date_number; i++) {
            AddDate add = new AddDate();
            add.setOperands(String.valueOf(fulldate), 0, 0, i);

            int addDay = add.get_day();
            int addDate = add.get_date();

            int lunch_month = ((addDate - 20210000) / 100);
            int lunch_date = (addDate - 20210000 - lunch_month * 100);
            createtv(lunch_month, lunch_date, addDay);
        }
    }

    public void createtv(int b, int c, int daynum) {//createtv(7,15);=7월 15일급식출력
        LinearLayout lunch_menu_linear = new LinearLayout(getApplicationContext());
        TextView tv_lunch_date = new TextView(getApplicationContext());
        TextView tv_lunch = new TextView(getApplicationContext());

        dada = day[daynum];
        //Log.d("로그-출력요일",dada);
        tv_lunch_date.setText(b + "월 " + c + "일 (" + dada + ")");
        tv_lunch_date.setTextSize(13);
        tv_lunch_date.setTextColor(Color.rgb(0, 0, 0));

        tv_lunch.setText(lunch[b][c]);
        tv_lunch.setTextSize(13);
        tv_lunch.setTextColor(Color.rgb(0, 0, 0));


        LinearLayout.LayoutParams Linear_par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Linear_par.rightMargin = 30;
        lunch_menu_linear.setLayoutParams(Linear_par);
        lunch_menu_linear.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams date_par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        date_par.gravity = Gravity.CENTER_HORIZONTAL;
        date_par.topMargin = 25;
        tv_lunch_date.setLayoutParams(date_par);

        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_lunch.setLayoutParams(par);


        tv_lunch_date.setPadding(50, 5, 50, 5);

        tv_lunch.setWidth(LunchTextView_Width);
        tv_lunch.setPadding(20, 30, 20, 0);
        tv_lunch.setGravity(Gravity.CENTER);


        if (first_lunch_view == 10) {
            tv_lunch_date.setBackground(ContextCompat.getDrawable(this, R.drawable.lunch_date_background1));
            lunch_menu_linear.setBackground(ContextCompat.getDrawable(this, R.drawable.lunch_background1));
            first_lunch_view = 0;
        } else {
            tv_lunch_date.setBackground(ContextCompat.getDrawable(this, R.drawable.lunch_date_background0));
            lunch_menu_linear.setBackground(ContextCompat.getDrawable(this, R.drawable.lunch_background));
        }

        LinearLayout lunchview = findViewById(R.id.lunchview);

        lunchview.addView(lunch_menu_linear);

        lunch_menu_linear.addView(tv_lunch_date);
        lunch_menu_linear.addView(tv_lunch);
    }

    public class table_api extends AsyncTask<String, Void, String> {//시간표 json 파일을 Shared에 저장하고 set talbe실행
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

        protected void onPostExecute(String result) {
            //저장
            SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.putString("table_json", receiveMsg);
            preferencesEditor.apply();

            //시간표 배치
            set_table();

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

        protected void onPostExecute(String result) {
            SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.putString("lunch_json", receiveMsg);
            preferencesEditor.apply();
            set_lunch();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();


                    Uri file = data.getData();
                    StorageReference riversRef = storageRef.child("lunch_menu/" + String.valueOf(fulldate) + ".jpg");
                    UploadTask uploadTask = riversRef.putFile(file);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(MainActivity.this, "사진 업로드 살패", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "사진 업로드 성공", Toast.LENGTH_LONG).show();


                            iv_image.setImageBitmap(img);
                        }
                    });

                    //서브 저장
                    Date cu_lunch = Calendar.getInstance().getTime();
                    SimpleDateFormat mrealfulldate_lunch = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss,SSSS", Locale.getDefault());
                    String realfulldate_lunch = mrealfulldate_lunch.format(cu_lunch);
                    StorageReference riversRef11 = storageRef.child("lunch_file/" + realfulldate_lunch + ".jpg");
                    UploadTask uploadTask11 = riversRef11.putFile(file);

                    uploadTask11.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }


        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in1 = getContentResolver().openInputStream(data.getData());

                    Bitmap img1 = BitmapFactory.decodeStream(in1);
                    in1.close();


                    Uri file1 = data.getData();
                    StorageReference riversRef1 = storageRef.child("banner/" + 1 + ".jpg");
                    UploadTask uploadTask1 = riversRef1.putFile(file1);

                    uploadTask1.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(MainActivity.this, "사진 업로드 살패", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "사진 업로드 성공", Toast.LENGTH_LONG).show();

                            iv_banner.setImageBitmap(img1);
                        }
                    });

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }


        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in1 = getContentResolver().openInputStream(data.getData());

                    Bitmap img1 = BitmapFactory.decodeStream(in1);
                    in1.close();


                    Uri file1 = data.getData();
                    StorageReference riversRef1 = storageRef.child("congress/" + 1 + ".jpg");
                    UploadTask uploadTask1 = riversRef1.putFile(file1);

                    uploadTask1.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(MainActivity.this, "사진 업로드 살패", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "사진 업로드 성공", Toast.LENGTH_LONG).show();

                            iv_congress.setImageBitmap(img1);
                        }
                    });

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onResume() {//재시작시
        super.onResume();
        Log.d(TAG, "onResume: 재시작");

        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);
        Setting_To_Main = mPreferences.getInt("setting_To_Main", 1);
        preferencesEditor.putInt("setting_To_Main", 1);
        preferencesEditor.apply();

        if (Setting_To_Main == 100) {

            //기본 시간표
            parse par = new parse();
            par.setgrade(grade, clas);
            timetable_original = Array_tableOriginal(grade, clas);
            for (int i = 1; i <= 7; i++) {
                for (int j = 1; j <= 5; j++) {
                    tv[i][j].setText(timetable_original[i][j]);

                }
            }
            //Log.d("로그 세팅투메인=", String.valueOf(mPreferences.getInt("setting_To_Main", 0)));
            //Log.d("로그 Setting_To_Main=", String.valueOf(Setting_To_Main));
            Log.d("로그", "방금 설정 들어감");
            timetable_original = Array_tableOriginal(grade, clas);
            //시간표 함수 실행
            table_api tableApi = new table_api();
            tableApi.execute(String.valueOf(grade), String.valueOf(clas));
            set_perform();
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


    public void set_perform_clear(){

        for (int grade=1;grade<=3;grade++){
            for(int clas=1;clas<=10;clas++){


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(grade+"_"+clas);
                myRef.setValue("텍스트를 눌러 편집후 저장 버튼을 눌러 학급일정을 저장해주세요\n\n\n\n\n");

            }
        }

    }


}


