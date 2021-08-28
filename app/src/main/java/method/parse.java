package method;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class parse {
    int grade, clas;

    //parse.json은 AsyncTask안에서 사용하기
    public static String json(String url) {
        String receiveMsg = " ";
        String str;

        try {
            URL parse_url = new URL(url);

            Log.d("로그", "json: " + url);
            HttpURLConnection conn = (HttpURLConnection) parse_url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                reader.close();

            } else {
                Log.i("로그-통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }

    public void setgrade(int grade, int clas) {
        this.grade = grade;
        this.clas = clas;
    }


}
