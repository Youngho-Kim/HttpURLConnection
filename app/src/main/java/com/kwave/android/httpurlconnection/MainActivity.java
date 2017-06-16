package com.kwave.android.httpurlconnection;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.kwave.android.httpurlconnection.domain.Data;
import com.kwave.android.httpurlconnection.domain.Row;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskInterface, OnMapReadyCallback {

    /** 기초정보
     * url : http://openAPI.seoul.go.kr:8088/6542794c546b77613330477a4a6279/json/SearchPublicToiletPOIService/1/5/
     * 인증키 : 6542794c546b77613330477a4a6279
     *
     * http://openAPI.seoul.go.kr:8088/6542794c546b77613330477a4a6279/json/SearchPublicToiletPOIService/1/5/
     */
    static final String URL_PREFIX = "http://openAPI.seoul.go.kr:8088/";
    static final String URL_CERT = "6542794c546b77613330477a4a6279";
    static final String URL_MID = "/json/SearchPublicToiletPOIService";

    // 한 페이지에 불러오는 데이터 수
    static final int PAGE_OFFSET = 10;
//    int pageBegin = 1;
//    int pageEnd = 10;
    int page = 0;

    ListView listView;
    TextView textView;
//    String url = "http://google.com";
    String url = "";

    // 어댑터
    ArrayAdapter<String> adapter;

    // 어댑터에서 사용할 데이터 공간
    final List<String> datas = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setListener();
        setMap();

// 최초 호출시 1번부터 10개의 데이터를 불러온다

//        try {

//            String result = getData(url);
//            Log.i("Result",result);
//             Remote.newTask(this);
//        } catch (Exception e) {
//            Log.e("Network", e.toString());
//            Toast.makeText(this, "네트워크 오류 : " +e.getMessage(), Toast.LENGTH_SHORT).show();
//        }

    }


    private void setViews(){
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        // 데이터 - 위에서 공간 할당 됨

        // 어댑터
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, datas);
        // 건물의 이름을 listView에 세팅
        listView.setAdapter(adapter);
    }

    private void setListener(){
        // 스크롤의 상태값을 체크해주는 리스너
        listView.setOnScrollListener(scrollListener);
    }

    private void setMap(){

    // 맵을 세팅
    FragmentManager manager = getSupportFragmentManager();
    SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.mapView);
    // 로드되면 onReady 호출하도록
        mapFragment.getMapAsync(this);
    }

    // 리스트의 가장 마지막 셀이 보이는지 여부
    boolean lastItemVisible = false;
    // 스크롤 리스너
   AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

        @Override
        // 스크롤에 변경 사항이 있을 경우
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisible){
                loadPage();
            }
        }

        @Override
        // 스크롤이 실행됐을 때
        // firstVisibleItem : 현재 화면에 보여지는 아이템의 index(번호)
        // visibleItemCount : 현재 화면에 보여지는 아이템의 개수
        // totalItemCount : 리스트의 담겨있는 전체 아이템의 개수
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d("ListView", "firstVisibleItem = "+firstVisibleItem);
//                Log.d("ListView", "visibleItemCount = "+visibleItemCount);
//                Log.d("ListView", "totalItemCount = "+totalItemCount);

            if(totalItemCount <= firstVisibleItem+visibleItemCount){
                lastItemVisible = true;
            }
            else{
                lastItemVisible = false;
            }
        }
    };


    private void loadPage(){
//        Log.i("Scroll", "현재 스크롤이 IDLE 상태입니다");
        nextPage();
//                    setUrl(pageBegin, pageEnd);
        setUrl();
        Remote.newTask(MainActivity.this);
    }



    //    private void setPage(int page){
//        pageEnd = page * PAGE_OFFSET;
//        pageBegin = pageEnd - PAGE_OFFSET + 1;
//    }
    private void nextPage(){
        page = page+1;
    }

    private void setUrl(){
/**
 *

        // string
        // stringBuffer
        // stringBuilder

        // string 연산.........
        // String result = "문자열" + "문자열" + "문자열";
        //                  -----------------
        //                    메모리공간 할당
        //                  ----------------------------
        //                          메모리 공간 할당


        StringBuffer sb = new StringBuffer();
        // 여러개의 쓰레드가 sb를 공통적으로 쳐다보고 있을때 서로 바꾸려고 함 -> 동기화를 지원
        sb.append("문자열");
        sb.append("문자열");

        StringBuilder sbl = new StringBuilder();    // 가장 빠름    // 여러개의 쓰레드가 sb를 공통적으로 쳐다보고 있을때 서로 바꾸려고 함 -> 동기화를 미지원
        sbl.append("문자열");
        sbl.append("문자열");
 */
        int end = page * PAGE_OFFSET;
        int begin = end - PAGE_OFFSET +1;
        url = URL_PREFIX + URL_CERT + URL_MID + "/" +begin+"/"+end;
    }



    @Override
    public String getUrl(){
        return url;
    }
    @Override
    public void postExecute(String jsonString){

        Data data = convertJson(jsonString);

        int total_count = data.getSearchPublicToiletPOIService().getList_total_count();
        Row[] items = data.getSearchPublicToiletPOIService().getRow();

        setItemCount(total_count);
        // 2. class를 -> json String으로 변환
        // 결과처리
        addDatas(items);
        addMarkers(items);

        // 지도 컨트롤
        LatLng sinsa = new LatLng(37.516066, 127.019361);
        moveMapPosition(sinsa);

        // 그리고 adapter를 갱신해준다.
        adapter.notifyDataSetChanged();
    }

    private void moveMapPosition(LatLng position){
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
    }

    // datas에 데이터 더하기
    private void addDatas(Row[] rows) {
        // 네트워크에서 가져온 데이터를 꺼내서 datas에 담아준다.
        for (Row row : rows) {
            datas.add(row.getFNAME());
        }
    }

    // 지도에 마커 생성
    private void addMarkers(Row[] items){
        for (Row row : items) {
            // row를 돌면서 좌표의 마커를 생성한다.
            MarkerOptions marker = new MarkerOptions();
            LatLng tempCoord = new LatLng(row.getY_WGS84(), row.getX_WGS84());
            marker.position(tempCoord);
            Log.d("tempCoord", "==============================" + tempCoord);
            marker.title(row.getFNAME());

            myMap.addMarker(marker);
        }
    }
    private void setItemCount(int total_count){

        // 총개수를 화면에 세팅
        textView.setText("총 개수 : "+total_count);
    }

    private Data convertJson(String jsonString){
        Gson gson = new Gson();
        // 1. json String -> class로 변환
        return gson.fromJson(jsonString, Data.class);
    }

    GoogleMap myMap;
@Override
    public void onMapReady(GoogleMap googleMap){
    myMap = googleMap;
        // 최초 호출시 첫번째 집합을 불러온다.
//        setPage(pageBegin);
//        setPage(page);
//        setUrl(page, PAGE_OFFSET);
    loadPage();



    }


}
