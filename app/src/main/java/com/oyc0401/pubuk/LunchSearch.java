package com.oyc0401.pubuk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.oyc0401.pubuk.databinding.ActivityLunchSearchBinding;
import com.oyc0401.pubuk.databinding.ActivityMainBinding;
import com.oyc0401.pubuk.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import method.AddDate;
import method.parse;
import method.value;

public class LunchSearch extends AppCompatActivity {

    ActivityLunchSearchBinding binding;
    private static final String TAG = "로그";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //바인딩
        binding = ActivityLunchSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new lunch_api().execute("20210301", "20211201");


    }


    public class lunch_api extends AsyncTask<String, Void, String> {//급식 json 파일을 Shared에 저장하고 get table,set talbe실행
        private String receiveMsg;
        private String[][] arr = new String[14][40];

        protected void onPreExecute() {
            Log.d("로그", "lunch_api 시작");
        }


        @Override
        protected String doInBackground(String... params) {
            String date1 = params[0];
            String date2 = params[1];
            receiveMsg = parse.json(
                    "https://open.neis.go.kr/hub/mealServiceDietInfo?Key=59b8af7c4312435989470cba41e5c7a6&Type=json&pIndex=1&pSize=1000&ATPT_OFCDC_SC_CODE=J10&SD_SCHUL_CODE=7530072&MLSV_FROM_YMD=" + date1 + "&MLSV_TO_YMD=" + date2);
            arr = Array_lunch(receiveMsg);
            return receiveMsg;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            Log.d(TAG, "onPostExecute: 급식 메뉴 " + arr[7][4]);

            binding.btnSearch.setOnClickListener(v -> {
                ArrayList<Integer []> pickedlunchdate=new ArrayList<>();
                String pick=binding.etSearch.getText().toString();
                ArrayList<String> listItem = new ArrayList();

                for(int i=3;i<=12;i++){
                    for(int j=1;j<=31;j++){


                        String[] st = arr[i][j].split("\n");
                        //Log.d(TAG, "onPostExecute: " + st.length);
                        for (int k = 0; k < st.length; k++) {
                            //Log.d(TAG, "onPostExecute: "+i+"월 "+j+"일 "+(k+1)+"번째 급식: " + st[k]);
                            if(st[k].contains(pick)){
                                Log.d(TAG, "onPostExecute: "+i+"월 "+j+"일 "+(k+1)+"번째 급식: " + st[k]);

                                pickedlunchdate.add(new Integer[]{i,j});
                                listItem.add(+i+"월 "+j+"일 "+(k+1)+"번째 급식: " + st[k]);


                            }
                        }


                    }
                }


                for(int i=0;i<pickedlunchdate.size();i++){
                    Log.d(TAG, "onPostExecute: "+ Arrays.toString(pickedlunchdate.get(i)));
                }


                ArrayAdapter<String>  adapter ;
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_tv, listItem);
                binding.tvLunchsearchbar.setAdapter(adapter);

                binding.tvLunchsearchbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int which, long l) {

                        Log.d(TAG, "onItemClick: "+ Arrays.toString(pickedlunchdate.get(which)));
                        int lunchMonth=pickedlunchdate.get(which)[0];
                        int lunchDate=pickedlunchdate.get(which)[1];
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
                            intent.putExtra("lunchmenu",arr[lunchMonth][lunchDate]);
                            intent.putExtra("month",lunchMonth);
                            intent.putExtra("date",lunchDate);
                            startActivity(intent);

                    }
                });



            });


        }

        private String[][] Array_lunch(String json) {


            ArrayList<String[]> lunchfullarray = new ArrayList();

            String[][] arr = new String[14][40];
            String[][][] reTURN = new String[14][40][10];



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


}