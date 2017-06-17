package com.kwave.android.httpurlconnection;

/**
 * Created by kwave on 2017-06-12.
 */

public interface TaskInterface {
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
    public String getUrl();
    public void postExecute(String result);
}
