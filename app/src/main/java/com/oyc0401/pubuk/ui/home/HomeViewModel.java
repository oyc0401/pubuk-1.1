package com.oyc0401.pubuk.ui.home;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutionException;

import method.storage;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> lunch_json;
    private MutableLiveData<String> table_json;

    String receiveMsg_lunch, receiveMsg_table;

    public HomeViewModel() {

        storage st = new storage();
        try {
            receiveMsg_lunch = st.getJson_lunch();
            //receiveMsg_table = st.getJson_table();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lunch_json = new MutableLiveData<>();
        lunch_json.setValue(receiveMsg_lunch);

        table_json = new MutableLiveData<>();
        table_json.setValue(receiveMsg_table);


    }

    public LiveData<String> getTextTable() {
        return table_json;
    }
    public LiveData<String> getTextLunch() {
        return lunch_json;
    }


}


