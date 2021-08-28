package com.oyc0401.pubuk.ui.home;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Bitmap> img;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        img=new MutableLiveData<>();


        mText.setValue("This is home fragment");



    }

    public LiveData<String> getText() {
        return mText;
    }





}


