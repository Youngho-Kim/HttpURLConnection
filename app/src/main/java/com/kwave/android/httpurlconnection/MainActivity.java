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
    static final String URL_PREFIX = "http://openAPI.seoul.go.kr:8088/";    // 인증키 전까지로 고정값임
    static final String URL_CERT = "6542794c546b77613330477a4a6279";        // 인증키
    static final String URL_MID = "/json/SearchPublicToiletPOIService";     // 서비스명

    // 한 페이지에 불러오는 데이터 수
    static final int PAGE_OFFSET = 10;
//    int pageBegin = 1;    // 처음 불러올 페이지
//    int pageEnd = 10;     // 마지막 불러올 페이지
    int page = 0;         //

    ListView listView;
    TextView textView;
//    String url = "http://google.com";
    String url = "";

    // 데이터 - 위에서 공간 할당 됨
    // 어댑터에서 사용할 데이터 공간
    final List<String> datas = new ArrayList<>();

    // 어댑터
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setMap();
        setListener();

// 최초 호출시 1번부터 10개의 데이터를 불러온다

//        try {
//              String url = "http://google.com";
//            String result = getData(url);
//            Log.i("Result",result);
//             Remote.newTask(this);
//        } catch (Exception e) {
//            Log.e("Network", e.toString());
//            Toast.makeText(this, "네트워크 오류 : " +e.getMessage(), Toast.LENGTH_SHORT).show();
//        }

    }           //  onCreate 끝.





// ----------------------UI 세팅-----------------------------------------------------------------------------
    private void setViews(){
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        // 어댑터
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, datas);
        // 건물의 이름을 listView에 세팅
        listView.setAdapter(adapter);
    }

    private void setMap(){
        // 구글 맵을 사용하기 위해서는 OnMapReadyCallback을 인터페이스로 받는다.
        // 내 맵이 사용됐다는 것을 알려줘야하는데 그 함수가 getMapAsync 이다.
        // 현재 fragment를 view에 세팅했는데 여기서 사용된 클래스가 map이라는 것을 알려주고
        // 그 fragment가 메모리에 로드돼서 사용할 준비가 되면 onMapReady를 호출한다.

        // 맵뷰를 세팅
        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.mapView);
        // 로드되면 onReady 호출하도록
        mapFragment.getMapAsync(this);
    }

    private void setListener(){
        // 스크롤의 상태값을 체크해주는 리스너
        listView.setOnScrollListener(scrollListener);
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
//---------------------------------------------------------------------------------------------------------------------




        @Override
        // 스크롤이 실행됐을 때
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d("ListView", "firstVisibleItem = "+firstVisibleItem);
               // firstVisibleItem : 현재 화면에 보여지는 아이템의 index(번호)
//                Log.d("ListView", "visibleItemCount = "+visibleItemCount);
                // visibleItemCount : 현재 화면에 보여지는 아이템의 개수
//                Log.d("ListView", "totalItemCount = "+totalItemCount);
                // totalItemCount : 리스트의 담겨있는 전체 아이템의 개수
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
        Task.newTask(MainActivity.this);
    }






//-----------페이지 설정------------------------------------------------------------
    //    private void setPage(int page){
    // 만약 화면에 보이는 것(page)이 10페이지라고 할 경우
    // pagebegin은 1,11,21,31,41 등 10의 자리 숫자가 된다.
    // pageEnd는 10,20,30,40,50등 page에 begin을 곱한 값이 된다.
//        pageEnd = page * PAGE_OFFSET;
//        pageBegin = pageEnd - PAGE_OFFSET + 1;
//    }
    private void nextPage(){
        // 다음에 갈 페이지를 미리 저장해놓는다.
        page = page+1;
    }
//------------------------------------------------------------------------------------------






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
        // 문자열을 계속해서 나열하면 메모리 공간을 지속적으로 할당하여 연산속도가 아주느려진다.
        // 그래서 나온 것이  StringBuffer와 StringBuilder.

        StringBuffer sb = new StringBuffer();
        // 여러개의 쓰레드가 sb를 공통적으로 쳐다보고 있을때 서로 바꾸려고 함 -> 동기화를 지원
        sb.append("문자열");
        sb.append("문자열");
         => "문자열문자열"


        StringBuilder sbl = new StringBuilder();
        // 가장 빠름
        // 여러개의 쓰레드가 sbl을 공통적으로 쳐다보고 있을때 서로 바꾸려고 함 -> 동기화를 미지원
        sbl.append("문자열");
        sbl.append("문자열");
        =>"문자열문자열"


    * 특별하게 복잡하지 않은 String 연산은 컴파일 타임시에 전부다 StringBuilder로 코드가 자동으로 바뀐다.
 */
        int end = page * PAGE_OFFSET;
        int begin = end - PAGE_OFFSET +1;
        url = URL_PREFIX + URL_CERT + URL_MID + "/" +begin+"/"+end;
    }






//----------------------InterFace에서 받아와 실행하는 부분----------------------------------------------------------------
    @Override
    public String getUrl(){
        return url;
    }
    @Override
    public void postExecute(String jsonString){

        Data data = convertJson(jsonString);

        // SearchPublicToiletPOIService 클래스에 있는 리스트의 총 개수
        int total_count = data.getSearchPublicToiletPOIService().getList_total_count();
        // Row 클래스에 있는 것들을 가져와서 배열로 담음. Row 클래스에 있던 것들이기에 타입은 ROW[]가 된다.
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
//-----------------------------------------------------------------------------------------------------------------------







//---------------------------postExecute의 실행 함수들-------------------------------------------------------------------
    private Data convertJson(String jsonString){
        Gson gson = new Gson();
        // 1. json String -> class로 변환
        return gson.fromJson(jsonString, Data.class);
    }

    private void setItemCount(int total_count){

        // 총개수를 화면에 세팅
        textView.setText("총 개수 : "+total_count);
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
            // row를 돌면서 화장실 하나하나의 좌표의 마커를 생성한다.
            MarkerOptions marker = new MarkerOptions();
            LatLng tempCoord = new LatLng(row.getY_WGS84(), row.getX_WGS84());  // 입력되는 좌표를 가진 객체만들기.
            // LatLng이 인자로 받는 값은 double이므로 좌표의 타입도 double을 바꿔준다.
            // tempCoord는 LatLng의 속성 중 좌표값을 갖고 있는 데이터를 갖고 있다.
            marker.position(tempCoord);
            Log.d("tempCoord", "==============================" + tempCoord);
            marker.title(row.getFNAME());

            myMap.addMarker(marker);    // 구글 지도에 마커 더하기
        }
    }

    private void moveMapPosition(LatLng position){
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));  // 구글 지도에 카메라를 입력되는 위치로 이동
    }

//--------------------------------------------------------------------------------------------------------------------

    // 구글 맵을 사용하기 위해서는 OnMapReadyCallback을 인터페이스로 받는다.
    // 내 맵이 사용됐다는 것을 알려줘야하는데 그 함수가 getMapAsync 이다.
    // 현재 fragment를 view에 세팅했는데 여기서 사용된 클래스가 map이라는 것을 알려주고
    // 그 fragment가 메모리에 로드돼서 사용할 준비가 되면 onMapReady를 호출한다.
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
