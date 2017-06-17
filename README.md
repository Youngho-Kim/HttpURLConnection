# HttpURLConnection

### 구글맵 띄우기
// 맵뷰를 세팅
[전체소스코드](https://github.com/Youngho-Kim/HttpURLConnection/blob/master/app/src/main/java/com/kwave/android/httpurlconnection/MainActivity.java)
```java
    FragmentManager manager = getSupportFragmentManager();
    SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.mapView);
    // 로드되면 onReady 호출하도록
        mapFragment.getMapAsync(this);
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
  
      private void loadPage(){
  //        Log.i("Scroll", "현재 스크롤이 IDLE 상태입니다");
          nextPage();
  //                    setUrl(pageBegin, pageEnd);
          setUrl();
          Remote.newTask(MainActivity.this);
      }
```


#### stringBuffer와 stringBuilder 비교
[전체소스코드](https://github.com/Youngho-Kim/HttpURLConnection/blob/master/app/src/main/java/com/kwave/android/httpurlconnection/MainActivity.java)

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






### 네트워크 처리 및 응답처리
[전체소스코드](https://github.com/Youngho-Kim/HttpURLConnection/blob/master/app/src/main/java/com/kwave/android/httpurlconnection/Remote.java)
<head 네트워크 처리/>

```java
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
```




### Interface
[전체소스코드](https://github.com/Youngho-Kim/HttpURLConnection/blob/master/app/src/main/java/com/kwave/android/httpurlconnection/TaskInterface.java)

<head> 
    <p3> 인터페이스를 사용하는 이유</p3>
</head>

    //newTask를 재사용하기 위해서는 넘겨주는 인자와 처리가 끝나고 호출되는 결과처리를 두가지로 분리해줘야한다.
    // newTask를 만들때 인자값을 직접 넘겨주지 않고 값을 처리하는 함수를 만들어서 넣어주면 된다.
    // 객체단위로 사용하기 위해 이렇게 사용한다.
    // 만약 아래의 함수가 다른 클래스로 옮겨가게 된다면 newTask함수의 인자값으로 아래 함수가 있는 클래스를 가져와야한다.
    // 그렇게 하면 onPostExecute나 doInBackground에서도 아래 함수들을 사용할 수 있게 된다.
    // 하지만 객체지향에서는 타입의 의존성이 생기면 안된다. 그래서 인터페이스를 사용한다.
    // TaskInterface를 구현한 클래스는 어떤 클래스라도 캐스팅을 통해서 이 함수들만 사용할 수 있게 된다.
    // 인자값이 인터페이스인 경우 인자값으로 넘어오는 클래스가 인터페이스를 구현했다는 것을 의미한다.
    // 즉, 인자값이 인터페이스라면 인터페이스를 구현한 어떤 클래스도 인자값으로 받을 수 있다는 걸 의미한다.
    // 단, 이때 그 클래스의 전체를 받지 못 하고 인터페이스만 사용할 수 있다.