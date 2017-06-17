package com.kwave.android.httpurlconnection;

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
    // 데이터를 네트워크로 가져오면 그 순간은 무조건 String 형태로 온다. 그래서 리턴 값을 String으로 해준다.
    // 데이터를 네트워크에서 가져오는 함수들은 Exception이 발생했을때 Log를 찍거나 해서 없애지 않고
    // throws를 통해서 넘겨서 요청한 곳에서 Exception 처리를 하게한다.
    // => 예외처리를 토스트로 하건 팝업으로 하건은 UI단에서 할 일이므로 여기서는 Exception이 났다는 알림을 전달해주고
    // 요청한 곳에서 토스트, 팝업, 로그등 알맞은 형태로 예외처리를 하게 한다.
    public static String getData(String url) throws Exception{     // <- 요청한 곳에서 Exception 처리를 해준다.
        // 여기서 static을 하는 이유는 getData가 실행이 됐을때 바로 메모리에 올라가져 있어야하기 때문이다.
        String result = "";


        // 네트워크 처리
        // 1. 요청처리 Request
        // (url을 가진 서버와 연결)
        // 1.1 url 객체 만들기
        URL serverUrl = new URL(url);
        // 1.2 연결 객체 생성
        HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();      // url 객체에서 연결을 꺼낸다.
        // HttpURLConnection : DBHelper와 비슷한 역할을 함.  서버와 연결을 해줌
        // 1.3 http method 결정
        con.setRequestMethod("GET");







        // 2. 응답처리 Response
        // 2.1 응답코드 분석
        int responseCode = con.getResponseCode();
        // 2.2 정상적인 응답처리
        if(responseCode == HttpURLConnection.HTTP_OK){  // 정상적인 코드 처리
            BufferedReader br = new BufferedReader(new InputStreamReader( con.getInputStream() ) );
            // 읽어온 데이터를 빠르게 처리하기 위해 BufferedReader를 사용하려하는데
            // con.getInputStream()이 BufferedReader의 형태로 읽기 위해서는 InputStreamReader로 한번 감싸주어야한다.
            // 또한, BufferedReader는 줄 단위로 읽을 수 있다.
            String temp = null;
            while ((temp = br.readLine()) != null){ //  BufferedReader에서 가져온 한줄의 데이터를 temp에 넣고 있다.
                result += temp;     // BufferedReader에서 한줄씩 꺼내온 데이터를 result에 모으고 있다.
            }
        }
        else{
            // 각자 호출측으로 Exception을 만들어서 넘겨주기
            Log.e("Network", "error Code = "+responseCode);
        }

        return result;
    }


}
