package com.oyc0401.pubuk.ui.home;


import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.oyc0401.pubuk.MainActivity;
import com.oyc0401.pubuk.R;
import com.oyc0401.pubuk.ScrollingActivity111;

import com.oyc0401.pubuk.databinding.FragmentHomeBinding;
import com.oyc0401.pubuk.setting;
import com.oyc0401.pubuk.ui.dashboard.DashboardViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import method.AddDate;
import method.parse;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

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


    TextView[][] tv = new TextView[10][10];
    ImageView iv_image, iv_banner, iv_congress, iv_congress2, iv_congress3, iv_congress4;//이미지
    int REQUEST_CODE = 0;

    Dialog dl_login; // 커스텀 다이얼로그
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    ActivityResultLauncher<String> mGetlunch = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    try {
                        InputStream in = getActivity().getContentResolver().openInputStream(uri);
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();

                        StorageReference riversRef = storageRef.child("lunch_menu/" + fulldate + ".jpg");
                        UploadTask uploadTask = riversRef.putFile(uri);
                        uploadTask.addOnFailureListener(exception ->
                                Toast.makeText(getActivity(), "사진 업로드 살패", Toast.LENGTH_LONG).show())
                                .addOnSuccessListener(taskSnapshot -> {
                                    Toast.makeText(getActivity(), "사진 업로드 성공", Toast.LENGTH_LONG).show();
                                    iv_image.setImageBitmap(img);
                                });

                        //서브 저장
                        Date cu_lunch = Calendar.getInstance().getTime();
                        SimpleDateFormat mrealfulldate_lunch = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss,SSSS", Locale.getDefault());
                        String realfulldate_lunch = mrealfulldate_lunch.format(cu_lunch);
                        StorageReference riversRef11 = storageRef.child("lunch_file/" + realfulldate_lunch + ".jpg");
                        UploadTask uploadTask11 = riversRef11.putFile(uri);

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
                    Log.d(TAG, "onActivityResult: rorororororo" + uri);
                }
            });

    ActivityResultLauncher<String> mGetbanner = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    try {
                        InputStream in = getActivity().getContentResolver().openInputStream(uri);
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();

                        StorageReference riversRef = storageRef.child("banner/" + 1 + ".jpg");
                        UploadTask uploadTask = riversRef.putFile(uri);
                        uploadTask.addOnFailureListener(exception ->
                                Toast.makeText(getActivity(), "사진 업로드 살패", Toast.LENGTH_LONG).show())
                                .addOnSuccessListener(taskSnapshot -> {
                                    Toast.makeText(getActivity(), "사진 업로드 성공", Toast.LENGTH_LONG).show();
                                    iv_image.setImageBitmap(img);
                                });
                    } catch (Exception e) {

                    }
                }
            });

    ActivityResultLauncher<String> mGetcomgress = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    try {
                        InputStream in = getActivity().getContentResolver().openInputStream(uri);
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();

                        StorageReference riversRef = storageRef.child("congress/" + 1 + ".jpg");
                        UploadTask uploadTask = riversRef.putFile(uri);
                        uploadTask.addOnFailureListener(exception ->
                                Toast.makeText(getActivity(), "사진 업로드 살패", Toast.LENGTH_LONG).show())
                                .addOnSuccessListener(taskSnapshot -> {
                                    Toast.makeText(getActivity(), "사진 업로드 성공", Toast.LENGTH_LONG).show();
                                    iv_image.setImageBitmap(img);
                                });

                    } catch (Exception e) {

                    }
                }
            });


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        onSaveInstanceState(savedInstanceState);






        Log.d("로그", "onCreateView: 홈 온크리에이트뷰뷰뷰뷰뷰뷰ㅠ");
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();





        homeViewModel.getTextTable().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onChange: 테이블");

                SharedPreferences mPreferences = getActivity().getSharedPreferences(SharedPrefFile, 0);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString("table_json",s);
                preferencesEditor.apply();
                set_table();
            }
        });

        homeViewModel.getTextLunch().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(TAG, "onChange: 런치");
                SharedPreferences mPreferences = getActivity().getSharedPreferences(SharedPrefFile, 0);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString("lunch_json", s);
                preferencesEditor.apply();
                set_lunch();

            }
        });









        check_oncreate = 1;
        iv_image = binding.ivImage;
        iv_banner = binding.ivBanner;
        iv_congress = binding.ivCongress1;
        iv_congress2 = binding.ivCongress2;
        iv_congress3 = binding.ivCongress3;
        iv_congress4 = binding.ivCongress4;
        //new DownloadFilesTask().execute("http://www.puchonbuk.hs.kr/upload/l_passquery/20210601_2.jpeg");//이미지


        Button btn_univ = binding.btnUniv;
        TextView lunch1 = binding.lunch;
        HorizontalScrollView hsv = binding.hsv;
        LinearLayout lunchview = binding.lunchview;


        SharedPreferences mPreferences = this.getActivity().getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);
        preferencesEditor.putInt("setting_To_Main", 1);
        login = mPreferences.getInt("login", 0);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size); // 화면사이즈 구하기
        width = size.x; // 너비
        height = size.y; // 높이구하기
        LunchTextView_Width = width * 3 / 8; // 급식표 사이즈 비율
////////////////////////////////////////////////////////////////////////////////////////////


        TextView tv_timetable321 = binding.tvTimetable321;
        timetable_original = Array_tableOriginal(grade, clas);

        // 시간표 findViewByIdF 하기
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                tv[i][j] = root.findViewById(getResources().getIdentifier("tt" + (i) + "_" + (j), "id", getActivity().getPackageName()));
            }
        }

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
        TextView tv_perform = binding.tvPerformTitle;
        tv_perform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(grade + "_" + clas);

                EditText et_perfomance = binding.etPerformance;
                myRef.setValue(et_perfomance.getText().toString());
                Toast.makeText(getActivity(), "저장되었습니다", Toast.LENGTH_SHORT).show();

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
                Glide.with(getActivity()).load(uri).into(iv_image);
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

        TextView tv_lunchPicture_title = binding.tvLunchPictureTitle;
        tv_lunchPicture_title.setText(month + "월 " + date + "일 급식사진");
        tv_lunchPicture_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login == 10 | login == 20) {
                    mGetlunch.launch("image/*");
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
                Glide.with(getActivity()).load(uri1).into(iv_banner);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        ImageView iv_banner = binding.ivBanner;
        TextView memo = binding.memo;
        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login == 20) {
                    mGetbanner.launch("image/*");
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
                        Intent intent = new Intent(Intent.ACTION_VIEW);
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
                Glide.with(getActivity()).load(uri1).into(iv_congress);
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
                Glide.with(getActivity()).load(uri1).into(iv_congress2);
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
                Glide.with(getActivity()).load(uri1).into(iv_congress3);
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
                Glide.with(getActivity()).load(uri1).into(iv_congress4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


        TextView tv_congress_title = binding.tvCongressTitle;
        tv_congress_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login == 20) {
                    mGetcomgress.launch("image/*");
                } else {
                    //Toast.makeText(MainActivity.this, "권한이 없습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //기본 시간표
        parse par = new parse();
        par.setgrade(grade, clas);

        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                tv[i][j].setText(timetable_original[i][j]);

            }
        }


        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("로그", "onCreateView: 홈 온크리에이트");
    }


    private String[][] Array_table() {
        String[][] arr = new String[10][8];

        for (int i = 0; i <= 7; i++) {//배열 초기화
            for (int j = 0; j <= 7; j++) {
                arr[i][j] = " ";//arr[가로줄][교시]
            }
        }

        SharedPreferences mPreferences = getActivity().getSharedPreferences(SharedPrefFile, 0);
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
        SharedPreferences mPreferences = getActivity().getSharedPreferences(SharedPrefFile, 0);
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

        AssetManager assetManager = getResources().getAssets();
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

        TextView tv_perform = binding.tvPerformTitle;
        tv_perform.setText(grade + "학년 " + clas + "반 일정");
        EditText et_perfomance = binding.etPerformance;

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
        LinearLayout lunch_menu_linear = new LinearLayout(getActivity().getApplicationContext());
        TextView tv_lunch_date = new TextView(getActivity().getApplicationContext());
        TextView tv_lunch = new TextView(getActivity().getApplicationContext());

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
            tv_lunch_date.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.lunch_date_background1));
            lunch_menu_linear.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.lunch_background1));
            first_lunch_view = 0;
        } else {
            tv_lunch_date.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.lunch_date_background0));
            lunch_menu_linear.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.lunch_background));
        }

        LinearLayout lunchview = getActivity().findViewById(R.id.lunchview);

        lunchview.addView(lunch_menu_linear);

        lunch_menu_linear.addView(tv_lunch_date);
        lunch_menu_linear.addView(tv_lunch);
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("로그", "onCreateView: 홈 크리에이티드");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("로그", "onCreateView: 홈 디스트로이");
        binding = null;
    }
}
