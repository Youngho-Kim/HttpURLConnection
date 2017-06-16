package com.kwave.android.httpurlconnection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kwave on 2017-06-12.
 */

public class Remote {

    // 인자로 받은  url로 네트워크를 통해 데이터를 가져오는 함수
    public static String getData(String url) throws Exception{     // <- 요청한 곳에서 Exception 처리를 해준다.
        String result = "";


        // 네트워크 처리
        // 1. 요청처리 Request
        // (url을 가진 서버와 연결)
        // 1.1 url 객체 만들기
        URL serverUrl = new URL(url);
        // 1.2 연결 객체 생성
        HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();      // url 객체에서 연결을 꺼낸다.
        // 1.3 http method 결정
        con.setRequestMethod("GET");

        // 2. 응답처리 Response
        // 2.1 응답코드 분석
        int responseCode = con.getResponseCode();
        // 2.2 정상적인 응답처리
        if(responseCode == HttpURLConnection.HTTP_OK){  // 정상적인 코드 처리
            BufferedReader br = new BufferedReader(new InputStreamReader( con.getInputStream() ) );
            String temp = null;
            while ((temp = br.readLine()) != null){
                result += temp;
            }
        }
        else{
            // 각자 호출측으로 Exception을 만들어서 넘겨주기
            Log.e("Network", "error Code = "+responseCode);
        }

        return result;
    }


    // Thread를 생성
    public static void newTask(final TaskInterface taskInterface){
        new AsyncTask<String, Void, String>(){

            @Override
            // 백그라운드 처리 함수
            protected String doInBackground(String... params) {
                String result = "";
                try {
                    // getData 함수로 데이터를 가져온다.
                    result = getData(params[0]);
//                    Log.i("Network",result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                // 결과처리
                taskInterface.postExecute(result);
            }
        }.execute(taskInterface.getUrl());
    }

}
