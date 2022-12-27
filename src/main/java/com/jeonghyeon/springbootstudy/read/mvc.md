# MVC 관련
## 스프링 부트에서 MVC
* 스프링 부트에서 바로 설정 없이 웹 프로젝트를 할 수 있는 이유는 WebMvcAutoConfiguration 이란 파일 때문이다.
  * 기본적 설정을 어떻게 처리 할 지 들어 있는 클래스

## HttpMessageConverters
* Request(JSON,...)를 객체로 또는 객체를 Response(JSON,...) 본문으로
* @RequestBody, @ResponseBody, @RestController