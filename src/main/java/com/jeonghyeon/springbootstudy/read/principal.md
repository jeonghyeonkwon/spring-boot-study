# 스프링 부트 원리
## 의존성 관리
* 스프링 부트는 버전을 명시하지 않아도 적절한 버전을 가져온다(스프링 관련된 라이브러리(spring-boot-start-* 만이고 )
* gradle은 없지만 maven 같은 경우는 spring-boot-start-parent에 가져올 버전이 명시되어져 있다.
  * 만약 그 parent가 아닌 다른 버전들을 써야 된다면 수정 가능하다.
### spring-starter-web 추가시
* json
* tomcat
* hibernate-validator(2.3.x 이상 부터는 사라짐)
* webmvc

### java 버전 및 스프링 버전 변경
* maven 기준
```xml
...
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.3.RELEASE</version>
    <relativePath/>
  </parent>
  <properties>
      <spring.version>5.0.6.RELEASE</spring.version>
      <java.version>1.8</java.version>
  </properties>
```

## 자동 설정 이해
* @SpringBootApplication은 밑에 3가지를 합친것
  * SpringBootConfiguration
  * ComponentScan
    * @Component가 붙인 애노테이션들을 스캔하는것
    * 자기 위치부터 하위 패키지까지 스캔  
    * @Configuration
    * @Repository
    * @Service
    * @Controller
    * @RestController
  * EnableAutoConfiguration
    * spring 메타파일
      * spring-boot-autoconfigure 프로젝트 밑에 spring.factories
        * 리스트들 안에는 다 @Configuration이 붙어 있다
          * @Conditional.... 붙어 있는거는 ~일때 등록한다 안한다로 구분하기 위한 애노테이션

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpApplication.class, args);
  }
}
```
### 웹 애플리케이션말고 만드는 법

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Application {
  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(Application.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);
  }
}
```
