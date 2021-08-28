package com.oyc0401.pubuk;

import static android.widget.Toast.makeText;
import static com.oyc0401.pubuk.R.drawable.*;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import method.value;

public class ScrollingActivity111 extends AppCompatActivity {

    int click1 = 0;
    String[] ARR = new String[210];

    int size = 150;//확대사이즈
    String unicode1 = "0000169";//기본 대학코드
    int unicode2 = 30;//기본 전형
    String url1 = "http://www.adiga.kr/kcue/ast/eip/eis/inf/stdptselctn/eipStdGenSlcIemWebView.do?sch_year=2021&univ_cd=";//어디가 url1
    String url2 = "&iem_cd=";//어디가 url2
    Dialog dilaog01; // 커스텀 다이얼로그
    ArrayAdapter<String> adapter;
    Toast toast;
    String TAG = "로그";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling111);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        //webView.setWebViewClient(new WebViewClientClass());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setTextZoom(size);

        TextView uni = findViewById(R.id.uni);
        TextView hak1 = findViewById(R.id.hak1);
        TextView hak2 = findViewById(R.id.hak2);
        TextView hak3 = findViewById(R.id.hak3);
        TextView plus = findViewById(R.id.plus);
        TextView minus = findViewById(R.id.minus);

        ARR = value.uni_list();

        dilaog01 = new Dialog(ScrollingActivity111.this);// Dialog 초기화
        dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dilaog01.setContentView(R.layout.dialig1);
        ListView lv = dilaog01.findViewById(R.id.lv);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_tv, ARR);
        lv.setAdapter(adapter);

        String SharedPrefFile = "com.example.android.SharedPreferences";
        SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

///////////////////////////////


        unicode1=mPreferences.getString("unicode1","0000169");
        unicode2=mPreferences.getInt("unicode2",30);
        uni.setText(mPreferences.getString("uniname","대학교 입시결과"));
        if(unicode2==30){
            hak1.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_left_touch));
            hak1.setTextColor(Color.WHITE);}
        else if(unicode2==31){
            hak2.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_middle_touch));
            hak2.setTextColor(Color.WHITE);
        }else if(unicode2==32){
            hak3.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_right_touch));
            hak3.setTextColor(Color.WHITE);
        }

        Arrays.sort(ARR);
        //dilaog01.show(); 시작시 다이얼로그

        //웹뷰 동작
        webView.loadUrl(url1 + unicode1 + url2 + unicode2);

        //대학버튼 클릭
        uni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dilaog01.show();
            }
        });

        //리스트뷰 클릭
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int which, long l) {

                click1 = 1;

                unicode1 = value.uni_code(ARR[which]);

                preferencesEditor.putString("unicode1",unicode1);
                preferencesEditor.putString("uniname",ARR[which]);
                preferencesEditor.apply();

                Log.d(TAG, "onItemClick: 대학입결 url: " + url1 + unicode1 + url2 + unicode2);

                webView.loadUrl(url1 + unicode1 + url2 + unicode2);

                uni.setText(ARR[which]);
                dilaog01.cancel();
            }
        });

        //화면 확대
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = 250;
                webView.getSettings().setTextZoom(size);
                toastShow("확대");
            }
        });

        //화면 축소
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = 150;
                webView.getSettings().setTextZoom(size);
                toastShow("축소");

            }
        });


        //학종 버튼설정

        hak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unicode2 = 30;
                webView.loadUrl(url1 + unicode1 + url2 + unicode2);

                hak1.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_left_touch));
                hak2.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_middle));
                hak3.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_right));
                hak1.setTextColor(Color.WHITE);
                hak2.setTextColor(Color.BLACK);
                hak3.setTextColor(Color.BLACK);
                toastShow("학종");
                preferencesEditor.putInt("unicode2",unicode2);
                preferencesEditor.apply();

            }
        });

        //교과 버튼설정
        hak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unicode2 = 31;
                webView.loadUrl(url1 + unicode1 + url2 + unicode2);

                hak1.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_left));
                hak2.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_middle_touch));
                hak3.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_right));
                hak1.setTextColor(Color.BLACK);
                hak2.setTextColor(Color.WHITE);
                hak3.setTextColor(Color.BLACK);
                toastShow("교과");
                preferencesEditor.putInt("unicode2",unicode2);
                preferencesEditor.apply();
            }
        });

        //정시 버튼설정
        hak3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unicode2 = 32;
                webView.loadUrl(url1 + unicode1 + url2 + unicode2);

                hak1.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_left));
                hak2.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_middle));
                hak3.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_right_touch));
                hak1.setTextColor(Color.BLACK);
                hak2.setTextColor(Color.BLACK);
                hak3.setTextColor(Color.WHITE);
                toastShow("정시");
                preferencesEditor.putInt("unicode2",unicode2);
                preferencesEditor.apply();

            }
        });

    }

    private void toastShow(String message) {

        // 토스트 메서드
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    //메뉴만들기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling_activity111, menu);
        return true;
    }

    //메뉴 클릭 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        switch (item.getItemId()) {
            //자세히보기 눌렀을시
            case R.id.unifull:

                intent.setData(Uri.parse("http://adiga.kr/kcue/ast/eip/eis/inf/stdptselctn/eipStdGenSlcIemCmprGnrl2.do?p_menu_id=PG-EIP-16001&chkUnivList=" + unicode1 + "&sch_year=2021"));

                startActivity(intent);
                break;

            //입학처 눌렀을시
            /*case R.id.uniurl:              (공개해야되서 가려놓음)
                switch (ARR[a]) {
                    case "서울대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "연세대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "고려대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "성균관대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "한양대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "서강대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "중앙대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "경희대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "외국어대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "서울시립대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "건국대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "동국대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "홍익대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "국민대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "숭실대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "세종대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "단국대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "인하대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "아주대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "인천대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "한양대학교(ERICA)":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "광운대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "명지대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "가톨릭대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "이화여자대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "부산대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "울산대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "한림대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "경북대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "전북대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "전남대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "충남대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "영남대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "경상대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "강원대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "부경대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "대구대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "인제대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "순천향대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "서울교육대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "숙명여자대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "서울과학기술대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "상명대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "덕성여자대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "서울여자대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "경인교육대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "한국교원대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "가천대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "충북대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                    case "경기대학교":
                        intent.setData(Uri.parse("https://admission.snu.ac.kr/"));
                        startActivity(intent);
                        break;
                }
                break;*/

            //뒤로가기 눌렀을시
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}