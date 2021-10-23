package com.oyc0401.pubuk;


import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {

    public MutableLiveData<String[][]> arr_table,arr_lunch,arrTodayTimeTable;
    public MutableLiveData<String> lunch_json, table_json;
    public MutableLiveData<Integer> grade, clas, login;
    public MutableLiveData<Uri> img_lunch, img_banner;


    public ViewModel() {
        arr_table =new MutableLiveData<>();// arr_table[교시][요일]
        arr_lunch =new MutableLiveData<>();
        arrTodayTimeTable=new MutableLiveData<>();


        grade = new MutableLiveData<>();
        clas = new MutableLiveData<>();
        login = new MutableLiveData<>();

        img_lunch = new MutableLiveData<>();
        img_banner = new MutableLiveData<>();


    }


}


