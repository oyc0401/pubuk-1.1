package com.oyc0401.pubuk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> lunch_json;
    private MutableLiveData<String> table_json;

    String receiveMsg_lunch="", receiveMsg_table="";

    public ViewModel() {

        lunch_json = new MutableLiveData<>();
        table_json = new MutableLiveData<>();

    }

    public LiveData<String> getTextTable() {
        return table_json;
    }
    public LiveData<String> getTextLunch() {
        return lunch_json;
    }

    public void setTextTable(String text) {
        table_json.setValue(text);
    }
    public void setTextLunch(String text) {
        lunch_json.setValue(text);
    }










}


