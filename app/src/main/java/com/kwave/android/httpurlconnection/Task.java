package com.kwave.android.httpurlconnection;

import android.os.AsyncTask;

import static com.kwave.android.httpurlconnection.Remote.getData;

/**
 * Created by kwave on 2017-06-12.
 */

public class Task {
    // Thread를 생성
    public static void newTask(final TaskInterface taskInterface){
        // 인자값이 인터페이스인 경우 인자값으로 넘어오는 클래스가 인터페이스를 구현했다는 것을 의미한다.

        // AsyncTask는 리턴타입이 올 수 없다.  => 서브쓰레드를 사용하기 때문에 결과가 나오기 전에 결과처리가 된다.
        // AsyncTask나 , Thread는 보통 리턴타입을 void로 사용한다.
        new AsyncTask<String, Void, String>(){
            // String - doInBackground 인자값
            // Void -onProgressUpdate의 인자
            // String -  doInBackground의 리턴값이자 onPostExecute의 인자값


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
                taskInterface.postExecute(result);      // TaskInterface에
            }
        }.execute(taskInterface.getUrl());
    }







    //newTask를 재사용하기 위해서는 넘겨주는 인자와 처리가 끝나고 호출되는 결과처리를 두가지로 분리해줘야한다.
    // newTask를 만들때 인자값을 직접 넘겨주지 않고 값을 처리하는 함수를 만들어서 넣어주면 된다.
    // 객체단위로 사용하기 위해 이렇게 사용한다.
    // 만약 아래의 함수가 다른 클래스로 옮겨가게 된다면 newTask함수의 인자값으로 아래 함수가 있는 클래스를 가져와야한다.
    // 그렇게 하면 onPostExecute나 doInBackground에서도 아래 함수들을 사용할 수 있게 된다.
    // 하지만 객체지향에서는 타입의 의존성이 생기면 안된다. 그래서 인터페이스를 사용한다.
//    public String getUrl(){
//        return "주소값";
//    }
//
//    public void postExecute(){
//        // 결과처리
//    }

}
