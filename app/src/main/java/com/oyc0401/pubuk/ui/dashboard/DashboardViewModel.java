package com.oyc0401.pubuk.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutionException;

import method.parse;
import method.storage;


public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> lunch_json;
    private MutableLiveData<String> table_json;

    String receiveMsg_lunch, receiveMsg_table;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");


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

    public LiveData<String> getText() {
        return mText;
    }


}

