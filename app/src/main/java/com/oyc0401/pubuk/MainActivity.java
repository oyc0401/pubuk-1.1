package com.oyc0401.pubuk;

import static com.oyc0401.pubuk.R.drawable.time1_black;
import static com.oyc0401.pubuk.R.drawable.time1_blue;
import static com.oyc0401.pubuk.R.drawable.time1_white;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

import method.AddDate;
import method.parse;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    int login=0;
    String TAG="로그";
    long backKeyPressedTime = 0;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);





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


