package com.oyc0401.pubuk.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.oyc0401.pubuk.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private String SharedPrefFile = "com.example.android.SharedPreferences";
    int grade;
    int clas, schoolcode;

    final String[] school_name = new String[]{"부천북고등학교", "도당고등학교", "원종고등학교"};
    final String[] grade_name = new String[]{"1학년", "2학년", "3학년"};
    final String[] clas1 = new String[]{"1반", "2반", "3반", "4반", "5반", "6반", "7반", "8반", "9반"};
    final String[] clas2 = new String[]{"1반", "2반", "3반", "4반", "5반", "6반", "7반", "8반", "9반", "10반"};
    final String[] nasin_list = new String[]{"선택취소", "1등급", "2등급", "3등급", "4등급", "5등급", "6등급", "7등급", "8등급", "9등급"};
    int a, b;
    TextView tv_st[][] = new TextView[8][10];
    String TAG = "로그";

    String dbName = "Data.db"; //database 파일명
    String tableName = "member"; // 표 이름

    SQLiteDatabase db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        SharedPreferences mPreferences = getActivity().getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        schoolcode = mPreferences.getInt("school_code", 7530072);
        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);

        Button setschool = binding.school;
        Button setgrade = binding.grade;
        Button setclas = binding.clas;
        //Button setscore = findViewById(R.id.score);
        Button ok = binding.ok;
        setgrade.setText(grade + "학년");
        setclas.setText(clas + "반");

        /*setschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder gradedlg = new AlertDialog.Builder(setting.this);
                gradedlg.setTitle("학교");

                gradedlg.setItems(school_name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (school_name[which]){
                            case "부천북고등학교":
                                schoolcode=7530072;
                                break;
                            case "도당고등학교":
                                schoolcode=7530471;
                                break;
                            case "원종고등학교":
                                schoolcode=7530107;
                                break;
                        }
                        setschool.setText(school_name[which]);
                        preferencesEditor.putInt("school_code", schoolcode);
                        preferencesEditor.apply();
                    }
                });
                gradedlg.show();
            }
        });*/


        int check = mPreferences.getInt("check", 0);
        if (check == 1) {
            AlertDialog.Builder gradedlg = new AlertDialog.Builder(getActivity());
            gradedlg.setTitle("학년");

            gradedlg.setItems(grade_name, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    grade = which + 1;
                    setgrade.setText(grade + "학년");
                    preferencesEditor.putInt("grade", grade);
                    preferencesEditor.apply();
                }
            });
            gradedlg.show();


            preferencesEditor.putInt("check", 2);
            preferencesEditor.apply();
        }

        setgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder gradedlg = new AlertDialog.Builder(getActivity());
                gradedlg.setTitle("학년");

                gradedlg.setItems(grade_name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        grade = which + 1;
                        setgrade.setText(grade + "학년");
                        preferencesEditor.putInt("grade", grade);
                        preferencesEditor.apply();


                    }
                });
                gradedlg.show();
            }
        });

        setclas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder clasdlg = new AlertDialog.Builder(getActivity());
                clasdlg.setTitle("반");
                if (grade <= 2) {
                    clasdlg.setItems(clas2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clas = which + 1;
                            setclas.setText(clas + "반");
                        }
                    });
                } else {
                    clasdlg.setItems(clas2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clas = which + 1;
                            setclas.setText(clas + "반");
                            preferencesEditor.putInt("class", clas);
                            preferencesEditor.apply();
                        }
                    });
                }
                clasdlg.show();
            }
        });

        /*ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putInt("grade", grade);
                preferencesEditor.putInt("class", clas);
                preferencesEditor.putInt("setting_To_Main", 100);
                Log.d("로그", "1로만ㅇ듦ㄴㅇㅇ");
                preferencesEditor.apply();

                finish();
            }
        });*/


        //내신 설정
/*
        int st[][] = new int[8][10];

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 7; j++) {
                try {
                    tv_st[i][j] = findViewById(getResources().getIdentifier("st" + (i) + "_" + (j), "id", this.getPackageName()));
                    tv_st[i][j].setTag(i * 10 + j);
                    int value = mPreferences.getInt("st" + i + "_" + j, 0);
                    if (value != 0) {
                        tv_st[i][j].setText(String.valueOf(value));
                        st[i][j] = value;
                    }
                } catch (Exception e) {
                }
            }
        }

        //클릭 리스너
        for (a = 1; a <= 5; a++) {
            for (b = 1; b <= 7; b++) {
                tv_st[a][b].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder clasdlg = new AlertDialog.Builder(setting.this);
                        set_scoretable(v, clasdlg, preferencesEditor);
                        clasdlg.show();
                        Toast.makeText(setting.this, "미구현", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


        db= this.openOrCreateDatabase(dbName, MODE_PRIVATE ,null); //db문서를 열거나, 없으면 만드는 작업

        //만들어진 DB파일에 "member"라는 이름으로 테이블 생성 작업 수행
        db.execSQL("CREATE TABLE IF NOT EXISTS "+tableName+"" +
                "( semester integer , score integer, number integer, subject integer);");



        Log.d(TAG, "onCreate second: " +  "CREATE TABLE IF NOT EXISTS "+tableName+"" +
                "( semester integer , score integer, number integer, subject integer);" );

        Log.d(TAG, "onCreate second: " + "INSERT INTO "+tableName+"(semester, score, number, subject) VALUES("+1+","+3+","+4+","+1+");");


        db.execSQL("INSERT INTO "+tableName+"(semester, score, number, subject) VALUES("+1+","+3+","+4+","+1+");");


        nasin nasin = new nasin();
        nasin.addNasin(1, 1, 4, 1);
        nasin.addNasin(1, 2, 4, 1);
        nasin.addNasin(2, 4, 4, 1);
        nasin.addNasin(2, 4, 4, 1);
        nasin.addNasin(2, 4, 4, 1);
        nasin.addNasin(2, 4, 4, 1);
        nasin.addNasin(2, 4, 4, 1);



        Log.d(TAG, "onCreate average: " + nasin.average_full());

        Log.d(TAG, "onCreate first: " + nasin.average(1));
        Log.d(TAG, "onCreate second: " + nasin.average(2));
        */


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}