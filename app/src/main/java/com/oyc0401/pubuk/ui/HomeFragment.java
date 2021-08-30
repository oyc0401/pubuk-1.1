package com.oyc0401.pubuk.ui;


import static com.oyc0401.pubuk.R.drawable.time1_black;
import static com.oyc0401.pubuk.R.drawable.time1_blue;
import static com.oyc0401.pubuk.R.drawable.time1_white;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oyc0401.pubuk.R;
import com.oyc0401.pubuk.ViewModel;
import com.oyc0401.pubuk.databinding.FragmentHomeBinding;

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

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;


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


    String dada;
    String TAG = "로그";


    String[][] timetable_original = new String[10][8];
    String[] day = {"", "일", "월", "화", "수", "목", "금", "토"};


    TextView[][] tv = new TextView[10][10];
    ImageView iv_image, iv_banner, iv_congress, iv_congress2, iv_congress3, iv_congress4;//이미지

    Dialog dl_login; // 커스텀 다이얼로그
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


/////////////////////////////////////////


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onSaveInstanceState(savedInstanceState);




        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //SharedPreferences 추가
        String SharedPrefFile = "com.example.android.SharedPreferences";
        SharedPreferences mPreferences = this.requireActivity().getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);
        login = mPreferences.getInt("login", 0);
        preferencesEditor.apply();

        //화면 비율 얻어오기
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size); // 화면사이즈 구하기
        width = size.x; // 너비
        height = size.y; // 높이구하기
        LunchTextView_Width = width * 3 / 8; // 급식표 사이즈 비율


        //실행창/////////////////////////////////////////////////////////


        //기본 시간표 배열
        timetable_original = Array_tableOriginal(grade, clas);

        // 시간표 findViewByIdF 하기
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                tv[i][j] = root.findViewById(getResources().getIdentifier("tt" + (i) + "_" + (j), "id", requireActivity().getPackageName()));
            }
        }

        //시간표가 안보이면 클릭
        binding.tvTimetable321.setOnClickListener(v -> {
            for (int i = 1; i <= 7; i++) {
                for (int j = 1; j <= 5; j++) {
                    tv[i][j].setText(timetable_original[i][j]);
                    tv[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });


        ///학급 일정 설정
        set_perform();
        TextView tv_perform = binding.tvPerformTitle;
        tv_perform.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(grade + "_" + clas);

            EditText et_perfomance = binding.etPerformance;
            myRef.setValue(et_perfomance.getText().toString());
            Toast.makeText(requireActivity(), "저장되었습니다", Toast.LENGTH_SHORT).show();

        });


        //사진 클릭 설정
        String lunch_title_name = month + "월 " + date + "일 급식사진";
        binding.tvLunchPictureTitle.setText(lunch_title_name);
        binding.tvLunchPictureTitle.setOnClickListener(v -> {
            if (login == 10 | login == 20) {
                Setlunch.launch("image/*");
            }
        });
        binding.memo.setOnClickListener(v -> {
            if (login == 20) {
                Setbanner.launch("image/*");
            }
        });
        binding.ivBanner.setOnClickListener(v -> {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("banner_url");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String value = dataSnapshot.getValue(String.class);
                    //Log.d("로그", "Value is: " + value);
                    intent.setData(Uri.parse(value));
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w("로그", "Failed to read value.", error.toException());
                }
            });


        });
        binding.tvCongressTitle.setOnClickListener(v -> {
            if (login == 20) {
                Setcomgress.launch("image/*");
            }
        });


        //기본 시간표 설정하기
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 5; j++) {
                tv[i][j].setText(timetable_original[i][j]);
            }
        }

        //뷰모델
        ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        //시간표, 급식 UI
        model.arr_table.observe(getViewLifecycleOwner(),strings -> set_table(strings));
        model.arr_lunch.observe(getViewLifecycleOwner(),strings -> set_lunch(strings));

        //로그인 UI
        model.login.observe(getViewLifecycleOwner(), integer -> set_login(integer));

        //급식사진 UI
        model.img_lunch.observe(getViewLifecycleOwner(), uri -> set_img_lunch(uri));

        //배너 UI
        model.img_banner.observe(getViewLifecycleOwner(), this::set_img_banner);

        //대회사진1 UI
        model.img_con1.observe(getViewLifecycleOwner(), this::setImgCon1);

        //대회사진2 UI
        model.img_con2.observe(getViewLifecycleOwner(), this::setImgCon2);

        //대회사진3 UI
        model.img_con3.observe(getViewLifecycleOwner(), this::setImgCon3);

        //대회사진4 UI
        model.img_con4.observe(getViewLifecycleOwner(), this::setImgCon4);




        return root;



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

            StringBuilder buffer = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
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





    public void createtv(int b, int c, int daynum, String[][] lunch_array) {//createtv(7,15);=7월 15일급식출력
        LinearLayout lunch_menu_linear = new LinearLayout(requireActivity().getApplicationContext());
        TextView tv_lunch_date = new TextView(requireActivity().getApplicationContext());
        TextView tv_lunch = new TextView(requireActivity().getApplicationContext());

        dada = day[daynum];
        //Log.d("로그-출력요일",dada);
        String tv_lunch_date_text=b + "월 " + c + "일 (" + dada + ")";
        tv_lunch_date.setText(tv_lunch_date_text);
        tv_lunch_date.setTextSize(13);
        tv_lunch_date.setTextColor(Color.rgb(0, 0, 0));

        tv_lunch.setText(lunch_array[b][c]);
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
            tv_lunch_date.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.lunch_date_background1));
            lunch_menu_linear.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.lunch_background1));
            first_lunch_view = 0;
        } else {
            tv_lunch_date.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.lunch_date_background0));
            lunch_menu_linear.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.lunch_background));
        }

        LinearLayout lunchview = requireActivity().findViewById(R.id.lunchview);

        lunchview.addView(lunch_menu_linear);

        lunch_menu_linear.addView(tv_lunch_date);
        lunch_menu_linear.addView(tv_lunch);
    }


    //파이어베이스에 사진 저장
    ActivityResultLauncher<String> Setlunch = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        try {
            StorageReference riversRef = storageRef.child("lunch_menu/" + fulldate + ".jpg");
            UploadTask uploadTask = riversRef.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(requireActivity(), "사진 업로드 성공", Toast.LENGTH_LONG).show();
                ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);
                model.img_lunch.setValue(uri);
            });

            //서브 저장
            Date cu_lunch = Calendar.getInstance().getTime();
            SimpleDateFormat mrealfulldate_lunch = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss,SSSS", Locale.getDefault());
            String realfulldate_lunch = mrealfulldate_lunch.format(cu_lunch);
            StorageReference riversRef11 = storageRef.child("lunch_file/" + realfulldate_lunch + ".jpg");
            UploadTask uploadTask11 = riversRef11.putFile(uri);
            uploadTask11.addOnSuccessListener(taskSnapshot -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    ActivityResultLauncher<String> Setbanner = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        try {
            StorageReference riversRef = storageRef.child("banner/" + 1 + ".jpg");
            UploadTask uploadTask = riversRef.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(requireActivity(), "사진 업로드 성공", Toast.LENGTH_LONG).show();
                ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);
                model.img_banner.setValue(uri);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    });

    ActivityResultLauncher<String> Setcomgress = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        try {
            StorageReference riversRef = storageRef.child("congress/" + 1 + ".jpg");
            UploadTask uploadTask = riversRef.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(requireActivity(), "사진 업로드 성공", Toast.LENGTH_LONG).show();
                ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);
                model.img_con1.setValue(uri);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    });




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("로그", "onCreateView: 홈 디스트로이");
        binding = null;
    }


    private void set_table(String[][] array) {//저장된 json파일을 시간표배열에 넣고 시간표 보여줌

        String a = array[1][1];
        String b = array[1][2];
        String c = array[1][3];
        String d = array[1][4];
        String e = array[1][5];

        if ((!a.equals(" ")) | (!b.equals(" ")) | (!c.equals(" ")) | (!d.equals(" ")) | (!e.equals(" "))) {
            //Log.d("로그-성공!", a);
            for (int i = 1; i <= 7; i++) {//시간표 설정
                for (int j = 1; j <= 5; j++) {
                    tv[i][j].setText(array[i][j]);
                    tv[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    if (!array[i][j].equals(timetable_original[i][j])) {
                        tv[i][j].setBackgroundColor(Color.parseColor("#FFFDE7"));
                    }

                }
            }
        }


    }

    private void set_lunch(String[][] array) {//저장된 json파일을 급식배열에 넣고 급식 보여줌

        int date_number = 30;
        first_lunch_view = 10;
        for (int i = 0; i <= date_number; i++) {
            AddDate add = new AddDate();
            add.setOperands(String.valueOf(fulldate), 0, 0, i);

            int addDay = add.get_day();
            int addDate = add.get_date();

            int lunch_month = ((addDate - 20210000) / 100);
            int lunch_date = (addDate - 20210000 - lunch_month * 100);
            createtv(lunch_month, lunch_date, addDay,array);
        }
    }

    private void set_img_banner(Uri uri) {
        ImageView iv;
        iv = binding.ivBanner;
        Glide.with(requireActivity()).load(uri).into(iv);
    }

    private void set_img_lunch(Uri uri) {
        ImageView iv = binding.ivImage;

        Glide.with(requireActivity()).load(uri).into(iv);
        //모양설정
        iv.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, iv.getWidth(), iv.getHeight(), 30);
            }
        });
        iv.setClipToOutline(true);
    }

    private void set_login(Integer integer) {
        Log.d(TAG, "onChange: 로그인 " + integer);
        if (integer == 0) {
            login = 0;
            binding.tvTime1.setBackground(ContextCompat.getDrawable(requireActivity(), time1_white));
        } else if (integer == 10) {
            login = 10;
            binding.tvTime1.setBackground(ContextCompat.getDrawable(requireActivity(), time1_blue));
        } else if (integer == 20) {
            login = 20;
            binding.tvTime1.setBackground(ContextCompat.getDrawable(requireActivity(), time1_black));
        }
    }

    private void set_perform() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(grade + "_" + clas);

        TextView tv_perform = binding.tvPerformTitle;
        String perform_title_name = grade + "학년 " + clas + "반 일정";
        tv_perform.setText(perform_title_name);
        EditText et_perfomance = binding.etPerformance;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d("로그", "Value is: " + value);
                et_perfomance.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("로그", "Failed to read value.", error.toException());
            }
        });
    }


    private void setImgCon1(Uri uri) {
        ImageView iv;
        iv = binding.ivCongress1;
        Glide.with(requireActivity()).load(uri).into(iv);
    }

    private void setImgCon2(Uri uri) {
        ImageView iv = binding.ivCongress2;
        Glide.with(requireActivity()).load(uri).into(iv);
    }

    private void setImgCon3(Uri uri) {
        ImageView iv = binding.ivCongress3;
        Glide.with(requireActivity()).load(uri).into(iv);
    }

    private void setImgCon4(Uri uri) {
        ImageView iv = binding.ivCongress4;
        Glide.with(requireActivity()).load(uri).into(iv);
    }
}
