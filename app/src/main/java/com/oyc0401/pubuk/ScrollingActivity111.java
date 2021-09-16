package com.oyc0401.pubuk;

import static com.oyc0401.pubuk.R.drawable.bg_hack_botton_left;
import static com.oyc0401.pubuk.R.drawable.bg_hack_botton_left_touch;
import static com.oyc0401.pubuk.R.drawable.bg_hack_botton_middle;
import static com.oyc0401.pubuk.R.drawable.bg_hack_botton_middle_touch;
import static com.oyc0401.pubuk.R.drawable.bg_hack_botton_right;
import static com.oyc0401.pubuk.R.drawable.bg_hack_botton_right_touch;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.oyc0401.pubuk.databinding.ActivityScrolling111Binding;


import method.value;

public class ScrollingActivity111 extends AppCompatActivity {
    /**
     * 변수 선언
     **/
    private ActivityScrolling111Binding binding;
    int size = 150; //확대사이즈
    String univCode;
    int univProcess;
    String firstUrl = "http://www.adiga.kr/kcue/ast/eip/eis/inf/stdptselctn/eipStdGenSlcIemWebView.do?sch_year=2021&univ_cd="; //어디가 url1
    String secondUrl = "&iem_cd="; //어디가 url2
    String univName = "대학교 입시결과 검색";
    String[] univNames = value.uni_list();
    Toast toast;
    String TAG = "로그";

    /**
     * OnCreate
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrolling111Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WebView webView = findViewById(R.id.webview);

        setBasic(webView);

        setDialog(webView);

        setWebViewEvent(webView);

    }

    /**
     * 메인 함수
     **/
    private void setBasic(WebView webView) {

        // SharedPreferences 세팅
        SharedPreferences mPreferences = getSharedPreferences("com.example.android.SharedPreferences", 0);
        univCode = mPreferences.getString("univCode", "0000169");
        univProcess = mPreferences.getInt("univProcess", 30);

        // 퉁바 세팅
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 웹뷰 세팅
        setBasicWebView(webView);
        String url = firstUrl + univCode + secondUrl + univProcess;
        webView.loadUrl(url);

        // 입시전형 버튼 디자인 세팅
        if (univProcess == 30) setUnivBtnDesign(1);
        else if (univProcess == 31) setUnivBtnDesign(2);
        else if (univProcess == 32) setUnivBtnDesign(3);

        // 대학 버튼 이름 설정
        univName = univNames[mPreferences.getInt("univwhich", 0)];
        binding.uni.setText(univName);
    }

    private void setDialog(WebView webView) {
        // Declare
        Dialog dialog = new Dialog(ScrollingActivity111.this);
        dialog.setContentView(R.layout.dialog_univlist);

        ListView univList = dialog.findViewById(R.id.lv);
        univList.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_tv, univNames));

        // OnClickEvent
        binding.uni.setOnClickListener(v -> dialog.show());

        univList.setOnItemClickListener((adapterView, view, which, id) -> {
            // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
            SharedPreferences mPreferences = getSharedPreferences("com.example.android.SharedPreferences", 0);
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();

            preferencesEditor.putInt("univwhich", which).apply();
            univName = univNames[which];
            binding.uni.setText(univName);

            replaceWebView(webView, value.uni_code(univNames[which]), univProcess);

            dialog.cancel();
        });
    }

    private void setWebViewEvent(WebView webView) {

        binding.plus.setOnClickListener(v -> zoom(webView, 250, "확대"));

        binding.minus.setOnClickListener(v -> zoom(webView, 150, "축소"));

        binding.hak1.setOnClickListener(v -> {
            setUnivBtnDesign(1);
            replaceWebView(webView, univCode, 30);
            showToastMessage("학종");
        });

        binding.hak2.setOnClickListener(v -> {
            setUnivBtnDesign(2);
            replaceWebView(webView, univCode, 31);
            showToastMessage("교과");
        });

        binding.hak3.setOnClickListener(v -> {
            setUnivBtnDesign(3);
            replaceWebView(webView, univCode, 32);
            showToastMessage("정시");
        });
    }

    /**
     * 함수들의 영역
     **/

    private void abc() {
    }

    /**
     * webView의 배율을 변경하고 토스트 메시지 소환하기
     **/
    private void zoom(WebView webView, int size, String text) {
        webView.getSettings().setTextZoom(size);
        showToastMessage(text);
    }

    /**
     * webView의 주소를 대학코드가 univCode이고 전형이 univProcess인 url로 바꾸기
     **/
    private void replaceWebView(WebView webView, String univCode, int univProcess) {

        SharedPreferences mPreferences = getSharedPreferences("com.example.android.SharedPreferences", 0);
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("univCode", univCode).apply();
        preferencesEditor.putInt("univProcess", univProcess).apply();

        this.univProcess = univProcess;
        this.univCode = univCode;
        String Url = firstUrl + univCode + secondUrl + univProcess;
        Log.d(TAG, "onItemClick: 대학입결 url: " + Url);
        webView.loadUrl(Url);
    }

    /**
     * 토스트 메시지 소환하기
     **/
    private void showToastMessage(String message) {
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 대학전형선택 버튼의 디자인을 초기화하기
     **/
    private void setBasicUnivBtnDesign() {
        binding.hak1.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_left));
        binding.hak2.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_middle));
        binding.hak3.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_right));
        binding.hak1.setTextColor(Color.BLACK);
        binding.hak2.setTextColor(Color.BLACK);
        binding.hak3.setTextColor(Color.BLACK);
    }

    /**
     * 대학전형선택 버튼의 디자인을 univProcess에 맞게 바꾸기
     **/
    private void setUnivBtnDesign(Integer univProcess) {
        setBasicUnivBtnDesign();
        switch (univProcess) {
            case 1:
                binding.hak1.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_left_touch));
                binding.hak1.setTextColor(Color.WHITE);
                break;
            case 2:
                binding.hak2.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_middle_touch));
                binding.hak2.setTextColor(Color.WHITE);
                break;
            case 3:
                binding.hak3.setBackground(ContextCompat.getDrawable(ScrollingActivity111.this, bg_hack_botton_right_touch));
                binding.hak3.setTextColor(Color.WHITE);
                break;
        }
    }

    /**
     * webView의 기본설정 적용
     **/
    private void setBasicWebView(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setTextZoom(size);
    }

    /**
     * 오버라이딩의 영역
     **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling_activity111, menu);
        return true;
    }

    /**
     * 메뉴 클릭 설정
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        switch (item.getItemId()) {

            // 입시전형 자세히 보기 누르면
            case R.id.univfull:
                intent.setData(Uri.parse("http://adiga.kr/kcue/ast/eip/eis/inf/stdptselctn/eipStdGenSlcIemCmprGnrl2.do?p_menu_id=PG-EIP-16001&chkUnivList=" + univCode + "&sch_year=2021"));
                startActivity(intent);
                break;

            // 대학교 홈페이지 누르면
            case R.id.univHomePage:

                String url = "https://admission.snu.ac.kr/";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;

            // 뒤로가기 누르면
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}