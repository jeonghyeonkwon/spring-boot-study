# MVC 관련
## 스프링 부트에서 MVC
* 스프링 부트에서 바로 설정 없이 웹 프로젝트를 할 수 있는 이유는 WebMvcAutoConfiguration 이란 파일 때문이다.
  * 기본적 설정을 어떻게 처리 할 지 들어 있는 클래스

## HttpMessageConverters
* Request(JSON,...)를 객체로 또는 객체를 Response(JSON,...) 본문으로
* @RequestBody, @ResponseBody, @RestController

## ViewResolve
* accept header에 따라 응답값을 다르게 보내준다
* 스프링 부트에서는 기본적으로 json 형식만 지원하고 xml로 응답을 보내주고 싶으면 라이브러리를 추가해야한다
  * HttpMessageConvertersAutoConfiguration가 자동으로 json형식 처리를 도와줌

```xml
<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
  <version>2.9.6</version>
</dependency>   
```