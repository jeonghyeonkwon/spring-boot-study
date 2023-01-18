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


### JConsole
* 터미널에 Jconsole을 입력하면 프로젝트들의 메모리, CPU 사용량 등을 볼 수 있다.
* 하지만 보는것이 불편하다.

### VisualVM
* JAVA 10 부터는 jvm에 포함 되지 않아 따로 설치해야 된다.
* Jconsole보다 보기 편하다

### 웹으로 보기
* 웹으로 보고싶으면 application.yml에 추가
```properties
management.endpoints.web.exposure.include=*
```

* 모두 노출하면 보안상 위험하므로 spring-security 적용해서 보기

### Spring-Boot-Admin
* 위에 것 처럼 웹에서 UI 제공
* 스프링 부트에서 제공하는 것이 아닌 오픈소스 프로젝트
* 두개의 서버가 있어야됨

* 어드민 서버
```xml
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency> 
```

```java
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnabledAdminServer
public class AdminApplication{
    public static void main(String[] args){
      ...
    }
}
```

* 클라이언트 서버
```xml
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
```

```properties
spring.boot.admin.client.url=http://localhost:8080
management.endpoints.web.exposure.include=*
```