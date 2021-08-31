package com.oyc0401.pubuk;


import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {

    public MutableLiveData<String[][]> arr_table,arr_lunch,arrTodayTimeTable;
    public MutableLiveData<String> lunch_json, table_json;
    public MutableLiveData<Integer> grade, clas, login;
    public MutableLiveData<Uri> img_lunch, img_banner, img_con1, img_con2, img_con3, img_con4;


    public ViewModel() {
        arr_table =new MutableLiveData<>();
        arr_lunch =new MutableLiveData<>();
        arrTodayTimeTable=new MutableLiveData<>();


        grade = new MutableLiveData<>();
        clas = new MutableLiveData<>();
        login = new MutableLiveData<>();

        img_lunch = new MutableLiveData<>();
        img_banner = new MutableLiveData<>();
        img_con1 = new MutableLiveData<>();
        img_con2 = new MutableLiveData<>();
        img_con3 = new MutableLiveData<>();
        img_con4 = new MutableLiveData<>();

    }


}


