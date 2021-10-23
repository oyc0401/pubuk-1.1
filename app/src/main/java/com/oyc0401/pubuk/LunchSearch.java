package com.oyc0401.pubuk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.oyc0401.pubuk.databinding.ActivityLunchSearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import method.parse;

public class LunchSearch extends AppCompatActivity {

    ActivityLunchSearchBinding binding;
    private static final String TAG = "로그";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //바인딩
        binding = ActivityLunchSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //비동기 작업 시작
        new lunch_api().execute("20210301", "20211201");
    }


    public class lunch_api extends AsyncTask<String, Void, String> {//급식 json 파일을 Shared에 저장하고 get table,set talbe실행
        private String receiveMsg;
        private String[][] allMenus = new String[14][40];

        protected void onPreExecute() {
            Log.d("로그", "급식 찾기 시작");
        }

        @Override
        protected String doInBackground(String... params) {
            //나이스 서버 급식 파싱
            String date1 = params[0];
            String date2 = params[1];
            receiveMsg = parse.json(
                    "https://open.neis.go.kr/hub/mealServiceDietInfo?Key=59b8af7c4312435989470cba41e5c7a6&Type=json&pIndex=1&pSize=1000&ATPT_OFCDC_SC_CODE=J10&SD_SCHUL_CODE=7530072&MLSV_FROM_YMD=" + date1 + "&MLSV_TO_YMD=" + date2);
            allMenus = Array_lunch(receiveMsg);
            Log.d(TAG, "onPostExecute: 5월 4일 급식 메뉴 " + allMenus[5][4]);
            return receiveMsg;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //확인버튼 클릭시
            binding.btnSearch.setOnClickListener(v -> {
                ArrayList<Integer []> PickedLunchDates=new ArrayList<>();
                String pick=binding.etSearch.getText().toString();
                ArrayList<String> listItem = new ArrayList();


                for(int i=3;i<=12;i++){
                    for(int j=1;j<=31;j++){
                        // 메뉴 나누기
                        String[] menus = allMenus[i][j].split("\n");

                        //선택한 메뉴와 같은것 고르기
                        for (int k = 0; k < menus.length; k++) {
                            if(menus[k].contains(pick)){
                                Log.d(TAG, "onPostExecute3: "+i+"월 "+j+"일 "+(k+1)+"번째 급식: " + menus[k]);
                                PickedLunchDates.add(new Integer[]{i,j});
                                listItem.add(+i+"월 "+j+"일 "+(k+1)+"번째 급식: " + menus[k]);
                            }
                        }
                    }
                }

                for(int i=0;i<PickedLunchDates.size();i++){
                    Log.d(TAG, "onPostExecute4: "+ Arrays.toString(PickedLunchDates.get(i)));
                }

                ArrayAdapter<String>  adapter ;
                adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_tv, listItem);
                binding.tvLunchsearchbar.setAdapter(adapter);

                //선택한 날짜 급식 보여주기
                binding.tvLunchsearchbar.setOnItemClickListener((adapterView, view, which, l) -> {
                    // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디


                    Log.d(TAG, "onItemClick: "+ Arrays.toString(PickedLunchDates.get(which)));
                    int lunchMonth=PickedLunchDates.get(which)[0];
                    int lunchDate=PickedLunchDates.get(which)[1];
                    String strMonth;
                    String strDate;

                    if(lunchMonth<10) strMonth="0"+lunchMonth;
                    else strMonth= String.valueOf(lunchMonth);

                    if(lunchDate<10) strDate="0"+lunchDate;
                    else strDate= String.valueOf(lunchDate);

                    Log.d(TAG, "onItemClick: "+ strMonth+" "+strDate);

                    Intent intent = new Intent(LunchSearch.this, LunchView.class);
                    String url="http://www.puchonbuk.hs.kr/upload/l_passquery/2021"+strMonth+strDate+"_2.jpeg";
                    Log.d(TAG, "onItemClick: url"+url);
                        intent.putExtra("uri", url);
                        intent.putExtra("lunchmenu", allMenus[lunchMonth][lunchDate]);
                        intent.putExtra("month",lunchMonth);
                        intent.putExtra("date",lunchDate);
                        startActivity(intent);

                });



            });

        }

    }

    private String[][] Array_lunch(String json) {
        //return으로 모든 날짜 급식메뉴 줌
        //[5][27]=5월 27일 메뉴

        ArrayList<String[]> lunchfullarray = new ArrayList();

        String[][] arr = new String[14][40];

        try {//json 문자열을 배열에 넣음
            JSONArray jarray1 = new JSONObject(json).getJSONArray("mealServiceDietInfo");
            JSONObject jobject1 = jarray1.getJSONObject(1);
            JSONArray jarray2 = jobject1.getJSONArray("row");
            Log.d(TAG, "Array_lunch: " + jarray2.length());

            for (int i = 0; i <= jarray2.length(); i++) {

                JSONObject jobject2 = jarray2.getJSONObject(i);
                String MLSV_YMD = jobject2.optString("MLSV_YMD");
                String DDISH_NM = jobject2.optString("DDISH_NM");
                DDISH_NM = DDISH_NM.replace("<br/>", "\n");//문자열 바꾸기
                DDISH_NM = DDISH_NM.replace("-북고", "");//문자열 바꾸기
                DDISH_NM = DDISH_NM.replace(".", "");//문자열 바꾸기

                for (int j = 0; j <= 20; j++) {
                    DDISH_NM = DDISH_NM.replace(String.valueOf(j), "");//알레르기 문자열 바꾸기
                }

                lunchfullarray.add(new String[]{MLSV_YMD, DDISH_NM});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Array_ludasdasnch: "+lunchfullarray.size());

        //arr배열 초기화
        for (int i = 0; i <= 31; i++) {
            for (int j = 1; j <= 12; j++) {
                arr[j][i] = "급식 정보가 없습니다.";
            }
        }


        for (int i = 0; i < lunchfullarray.size(); i++) {//배열에 담긴 점심을 분류해서 lunch에 옮김
            int lunchdate = Integer.parseInt(lunchfullarray.get(i)[0]);
            if (lunchdate != 0) {
                int lunch_month = ((lunchdate - 20210000) / 100);
                int lunch_date = (lunchdate - 20210000 - lunch_month * 100);
                arr[lunch_month][lunch_date] = lunchfullarray.get(i)[1];
            }
        }

        return arr;
    }
}