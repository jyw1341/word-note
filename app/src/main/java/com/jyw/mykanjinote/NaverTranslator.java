package com.jyw.mykanjinote;

import android.util.Log;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class NaverTranslator {

    private static final String TAG = NaverTranslator.class.getSimpleName();

    public String translate(String content) {
        String clientId = "";
        String clientSecret = "";
        String result="error";
        StringBuffer response = null;
        try {
            String text = URLEncoder.encode(content, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            // post request
            String postParams = "source=ja&target=ko&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            Log.w(TAG,"check");
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;

            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            JSONObject jsonObject = new JSONObject(response.toString());
            Log.w(TAG,"jsonObject : "+jsonObject.toString());
            JSONObject jsonMessage = jsonObject.getJSONObject("message");
            Log.w(TAG,"jsonMessage : "+jsonMessage.toString());
            JSONObject jsonResult = jsonMessage.getJSONObject("result");
            result = jsonResult.getString("translatedText");
            br.close();
        } catch (Exception e) {
            Log.e(TAG,"/"+e);
        }
        return result;
    }
}
