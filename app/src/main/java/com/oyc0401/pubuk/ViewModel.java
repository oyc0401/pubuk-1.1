package com.oyc0401.pubuk;


import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> lunch_json,table_json,schedule;
    private MutableLiveData<Integer> grade,clas,login;
    private MutableLiveData<Uri> img_lunch,img_banner,img_con1,img_con2,img_con3,img_con4;

    public ViewModel() {
        lunch_json = new MutableLiveData<>();
        table_json = new MutableLiveData<>();
        grade=new MutableLiveData<>();
        clas=new MutableLiveData<>();
        login=new MutableLiveData<>();
        img_lunch=new MutableLiveData<>();
        img_banner=new MutableLiveData<>();
        img_con1=new MutableLiveData<>();
        img_con2=new MutableLiveData<>();
        img_con3=new MutableLiveData<>();
        img_con4=new MutableLiveData<>();

    }
    public LiveData<String> getTextTable() { return table_json; }
    public LiveData<String> getTextLunch() { return lunch_json; }
    public LiveData<Integer> getgrade() { return grade; }
    public LiveData<Integer> getclas() { return clas; }
    public LiveData<Integer> getlogin() { return login; }
    public LiveData<Uri> getImgLunch() { return img_lunch; }
    public LiveData<Uri> getImgBanner() { return img_banner; }
    public LiveData<Uri> getImgCon1() { return img_con1; }
    public LiveData<Uri> getImgCon2() { return img_con2; }
    public LiveData<Uri> getImgCon3() { return img_con3; }
    public LiveData<Uri> getImgCon4() { return img_con4; }


    public void setTextTable(String text) { table_json.setValue(text); }
    public void setTextLunch(String text) { lunch_json.setValue(text); }
    public void setIntGrade(Integer num) { grade.setValue(num); }
    public void setIntClass(Integer num) { clas.setValue(num); }
    public void setIntLogin(Integer num) { login.setValue(num); }
    public void setUriLunch(Uri uri) { img_lunch.setValue(uri); }
    public void setUriBanner(Uri uri) { img_banner.setValue(uri); }
    public void setUriCon1(Uri uri) { img_con1.setValue(uri); }
    public void setUriCon2(Uri uri) { img_con2.setValue(uri); }
    public void setUriCon3(Uri uri) { img_con3.setValue(uri); }
    public void setUriCon4(Uri uri) { img_con4.setValue(uri); }










}


