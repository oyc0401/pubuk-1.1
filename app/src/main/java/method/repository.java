package method;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class repository extends AppCompatActivity {
    String SharedPrefFile = "com.example.android.SharedPreferences";
    SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, 0);
    SharedPreferences.Editor preferencesEditor = mPreferences.edit();

    public void storeInteger(String key,Integer value){
        preferencesEditor.putInt(key, value);
        preferencesEditor.apply();
    }

    public void storeString(String key,String value){
        preferencesEditor.putString(key, value);
        preferencesEditor.apply();
    }

    public Integer getInteger(String key, Integer defValue){
        int value;
        value =mPreferences.getInt(key,defValue);
        return value;
    }

    public String getString(String key, String defValue){
        String value;
        value =mPreferences.getString(key,defValue);
        return value;
    }


}
