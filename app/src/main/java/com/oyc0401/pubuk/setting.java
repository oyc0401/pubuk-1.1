package com.oyc0401.pubuk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class setting extends AppCompatActivity {

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



    String dbName="Data.db"; //database 파일명
    String tableName="member"; // 표 이름

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        schoolcode = mPreferences.getInt("school_code", 7530072);
        grade = mPreferences.getInt("grade", 1);
        clas = mPreferences.getInt("class", 1);

        Button setschool = findViewById(R.id.school);
        Button setgrade = findViewById(R.id.grade);
        Button setclas = findViewById(R.id.clas);
        //Button setscore = findViewById(R.id.score);
        Button ok = findViewById(R.id.ok);
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
            AlertDialog.Builder gradedlg = new AlertDialog.Builder(setting.this);
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

                AlertDialog.Builder gradedlg = new AlertDialog.Builder(setting.this);
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

                AlertDialog.Builder clasdlg = new AlertDialog.Builder(setting.this);
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

        ok.setOnClickListener(new View.OnClickListener() {
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
        });


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


    }

    /*private void set_scoretable(View v, AlertDialog.Builder clasdlg, SharedPreferences.Editor preferencesEditor) {
        switch (v.getId()) {
            case R.id.st1_1:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("국어");
                break;
            case R.id.st1_2:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("수학");
                break;
            case R.id.st1_3:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("영어");
                break;
            case R.id.st1_4:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("통합사회");
                break;
            case R.id.st1_5:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("통합 과학");
                break;
            case R.id.st1_6:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("한국사");
                break;
            case R.id.st2_1:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("국어");
                break;
            case R.id.st2_2:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("수학");
                break;
            case R.id.st2_3:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("영어");
                break;
            case R.id.st2_4:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("통합사회");
                break;
            case R.id.st2_5:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("통합 과학");
                break;
            case R.id.st2_6:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("한국사");
                break;
            case R.id.st3_1:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("국어");
                break;
            case R.id.st3_2:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("수학");
                break;
            case R.id.st3_3:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("영어");
                break;
            case R.id.st3_4:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구1");
                break;
            case R.id.st3_5:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구2");
                break;
            case R.id.st3_6:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구3");
                break;
            case R.id.st3_7:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("확통");
                break;
            case R.id.st4_1:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("국어");
                break;

            case R.id.st4_2:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("수학");
                break;
            case R.id.st4_3:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("영어");
                break;
            case R.id.st4_4:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구1");
                break;
            case R.id.st4_5:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구2");
                break;
            case R.id.st4_6:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구3");
                break;
            case R.id.st4_7:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("확통");
                break;

            case R.id.st5_1:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("국어");
                break;
            case R.id.st5_2:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("수학");
                break;
            case R.id.st5_3:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("영어");
                break;
            case R.id.st5_4:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구1");
                break;
            case R.id.st5_5:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구2");
                break;
            case R.id.st5_6:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("탐구3");
                break;
            case R.id.st5_7:
                clasdlg.setItems(nasin_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_scoretable_inthe(which, v, preferencesEditor);
                    }
                });
                clasdlg.setTitle("확통");
                break;


        }
    }*/

    private void set_scoretable_inthe(int which, View v, SharedPreferences.Editor preferencesEditor) {
        int a1 = (int) v.getTag() / 10;
        int a2 = (int) v.getTag() % 10;
        if (which != 0) tv_st[a1][a2].setText(String.valueOf(which));
        else tv_st[a1][a2].setText(" ");
        preferencesEditor.putInt("st" + a1 + "_" + a2, which);
        preferencesEditor.apply();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt("grade", grade);
        preferencesEditor.putInt("class", clas);
        preferencesEditor.putInt("setting_To_Main", 100);
        Log.d("로그", "1로만ㅇ듦ㄴㅇㅇ");
        preferencesEditor.apply();

        finish();

    }
}

class nasin {
    int score[][] = new int[6][10];
    int dan[][] = new int[6][10];
    int level[] = {10, 9, 8, 7, 6, 5, 4, 3, 2};
    ArrayList<Integer[]> list = new ArrayList<Integer[]>();


    float 국어평균;
    float 영어평균;
    float 수학평균;
    float 사회평균;
    float 과학평균;


    public void addNasin(int 학기종류, int 내신등급, int 단위수, int 과목분류) {
        list.add(new Integer[]{학기종류, 내신등급, 단위수, 과목분류});
    }

    public float average_full() {
        int size = list.size();
        int sum = 0;
        int dan = 0;

        for (int i = 0; i <= size - 1; i++) {
            int a = list.get(i)[1];
            int b = list.get(i)[2];
            sum += a * b;
            dan += b;
        }
        float average = (float) sum / dan;
        return average;
    }

    public float average(int 학기) {
        int size = list.size();
        int sum = 0;
        int dan = 0;
        for (int i = 0; i <= size - 1; i++) {
            if (list.get(i)[0] == 학기) {
                int a = list.get(i)[1];
                int b = list.get(i)[2];
                sum += a * b;
                dan += b;
            }
        }
        float average = (float) sum / dan;
        return average;
    }






































}