#카카오 페이 URL Shortening 서버 작성

* 해결 전략으로 Base 62 인코딩 방식을 사용 하였습니다.
* 8글자 이내로 URL Key를 중복 없이 만들어 낼 방법은 없는 것으로 판단하였습니다.
* 그래서 8자가 넘을 경우 /xxx/xxxxxxxx 과 같은 형태로 데이터를 생성하는 방식으로 처리 하였습니다.
	* 다만 어느 정도 충돌이 날 수 있는 가능성은 있으나 MongoDB 와 같은 Bitwise 를 사용한 키를 만드는 방식도 고려는 해보았습니다.
	* 그러나 근본적으로 중복에 대한 해결책을 8자리 이하로 만들면서 할 수 있는 방법은 도메인을 추가로 따는 방법 외에는 없는 것으로 판단 됩니다.
	* 그러므로 최종 URL을 8자리 이하로 생성하는 전략을 사용하였습니다.
	* 또한 전략 패턴을 사용하여 URL Shortener 는 언제든지 대체할 수 있도록 구성하였습니다. 
* Spring Boot 2.0.1, Spring Web-Flux, MongoDB, Undertow Container, Freemarker 를 사용하였습니다.
	* 사용한 이유는 Web-Flux 기능이 비동기 Reactive 방식으로 성능적으로 매우 유리하고 함수형 프로그래밍을 적극적으로 사용할 수 있기 때문에 선택 하였습니다.
	* 또한 Insert가 매우 빠르고 Auto Sharding도 가능한 NoSQL인 MongoDB를 Persistent Layer로 사용하였습니다.
	* Undertow의 경우 Tomcat보다 빠르고 Servlet 기술에 Dependency가 없어서 Spring Boot의 기본 Container인 Tomcat 대신 사용하였습니다.
	* View Layer의 경우 큰 의미는 없지만 간단히 Template도 사용할 수 있는 사용하기 익숙한 Template Engine인 Freemarker를 사용하였습니다. 
* View 페이지 내부의 스크립트는 간단한 vue.js 와 axios를 이용한 Ajax call을 사용 하였고 URL 결과를 출력 및 redirect 할 수 있는 링크를 제공하였습니다.
* 서버 사이드 테스트는 Unit Test 및 Controller의 경우 Integration 테스트를 작성 하였습니다.
* 실행은 git clone 이후 
 * ./gradlew build 로 빌드후에
 * cd libs 로 폴더 이동후
 * java -jar payassignment-0.0.1-SNAPSHOT.jar 로 실행해 볼 수 있습니다.