# 운영 관련

## Spring Boot Actuator
* 실행 중인 애플리케이션 상태, 사용 중 메모리, 요청 횟수, 로깅 레벨을 확인 제어할 수 있다
* 다른 시스템과 외부 연동을 가능하다
  * 프로메테우스 등 ...
* /actuator로 접속하면 사용할 수 있는 url 정보를 알 수 있다
### 의존성 추가
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

```