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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



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

    /*private void set_scoretable_inthe(int which, View v, SharedPreferences.Editor preferencesEditor) {
        int a1 = (int) v.getTag() / 10;
        int a2 = (int) v.getTag() % 10;
        if (which != 0) tv_st[a1][a2].setText(String.valueOf(which));
        else tv_st[a1][a2].setText(" ");
        preferencesEditor.putInt("st" + a1 + "_" + a2, which);
        preferencesEditor.apply();
    }*/

    /*@Override
    public void onBackPressed() {
        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt("grade", grade);
        preferencesEditor.putInt("class", clas);
        preferencesEditor.putInt("setting_To_Main", 100);
        Log.d("로그", "1로만ㅇ듦ㄴㅇㅇ");
        preferencesEditor.apply();

        finish();

    }*/
}