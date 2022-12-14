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
public class SpringApplication {
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
or

```properties
spring.main.web-application-type=none
```

## 자동 설정 만들기
* 프로젝트 네이밍
  * Xxx-Spring-Boot-Autoconfigure 모듈 : 자동 설정
  * Xxx-Spring-Boot-Starter 모듈 : 필요한 의존성 정의

### 만들기 순서
1. 프로젝트 생성한다(메이븐 프로젝트)
2. 의존성 추가
```xml
...

<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure-processor</artifactId>
    <optional>true</optional>
  </dependency>
</dependencies>
<dependencyManagement>
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <type>pom</type>
    <scope>import</scope>
  </dependency>
</dependencies>
</dependencyManagement>
```
3. 설정 클래스를 만든다

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibConfiguration {

  @Bean
  public Account account(){
      Account account = new Account();
      account.setName("jeonghyeon");
      account.setAge(25);
      return account;
  }
}
```
4. src/main/resource/META-INF에 spring.factories 파일 만들기
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  설정 파일 풀경로(com.jeonghyeon...)
```
5. mvn install
   * 로컬 메이븐 저장소에 만들어 진다

#### 단점
  * 현재 프로젝트의 빈을 등록해도 외부의 설정으로 빈으로 덮어 씌어 진다 
    * ComponentScan으로 등록하고 AutoConfiguration으로 덮어 쓰기 때문에
      * 해결책
        * @ConditionalOnMissingBean
          * 빈이 없다면 이것을 사용하겠다는 것(덮어쓰기 방지)

```java


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public Account account() {
    Account account = new Account();
    account.setName("jeonghyeon");
    account.setAge(25);
    return account;
  }
}

```
#### 다른 해결법
  * 나는 의존하는 객체에서 몇개의 값만 바꾸고 싶다
  
  * 의존성 추가(외부 의존 프로젝트X)
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-configuration-processor</artifactId>
  <optional>true</optional>
</dependency> 
```
* application.properties 추가(외부 의존 프로젝트 X)
```properties
account.name = jeonghyeon
account.age = 31
```

* properties 만들기(외부 의존 프로젝트)

```java
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("account")
public class Account {
  private String name;
  private int age;

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Account{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
  }
}
```

* Configuration 수정(외부 의존 프로젝트)

```java
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccountProperties.class)
public class RibConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public Account account(AccountProperteis properties) {
    Account account = new Account();
    account.setName(properties.getName);
    account.setAge(properties.getAge);
    return account;
  }
}
```

## 내장 웹 서버 이해
* 스프링 부트는 웹서버가 아니다
  * 웹 어플리케이션 아니게 만들 수 있다.
  * 톰캣, 제티, 언더 토우 등 설정 가능
* 스프링 부트는 자동 설정되어 있다 밑에 코드처럼
  * ServletWebServerFactoryAutoConfiguration (서블릿 웹 서버 생성)
    * TomcatServletWebServerFactoryConfiguration (서블릿 웹 서버 생성)
  * DispatcherServletAutoConfiguration
    * 서블릿 만들고 등록
```java
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Application {
  public static void main(String[] args) {
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8090);
    Context context = tomcat.addContext("/", "/");
    HttpServlet servlet = new HttpServlet() {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head><title>");
        writer.println("Hey Tomcat");
        writer.println("</title></head>");
        writer.println("<body><h1>Hello Tomcat</h1></body>");
        writer.println("</html>");
      }
    };
    String servletName = "helloServley";
    tomcat.addServlet("/",servletName, servlet);
    context.addServletMappingDecoded("/hello",servletName);
    tomcat.start();
    tomcat.getServer().await();
  }
}
```
## 내장 웹 서버 응용 : 컨테이너와 서버 포트
* 기본은 tomcat이다

### 다른 서블릿 컨테이너 사용하기
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
      <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
  </dependency>
</dependencies>
```

### 랜덤 포트 주는법
```properties
server.port=0
```

#### 포트 번호 알기

```java

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@Component
public class PortListener implements ApplicationListener<ServletWebServerInitializedEvent> {
  @Override
  public void onApplicationEvent(ServletWebServerInitializedEvent event) {
    ServletWebServerApplicationContext applicationContext = event.getApplicationContext();
    System.out.println(applicationContext.getWebServer().getPort());
  }
}
```
## Https, Http2 적용
### Https 적용
1. 프로젝트 새로 만든다.
2. keystore.sh 만든다
```shell
keytool -genkey -alias spring -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validit 4000
```
3. 개인 정보를 입력한다
4. 그러면 keystore.p12가 생성
5. application.yml 작성
```yaml

server.ssl.key-store=키스토어_이름
server.ssl.key-store-type=PKCS12
server.ssl.key-store-password=설정때_입력한_비밀번호
server.ssl.key-alias=별칭_이름(여기서는_스프링)
```
6. https는 적용되었지만 공인된 인증서가 아니라 경고가 뜸
   * pub-key를 브라우저가 모름
7. 이제 http는 안됨
   * 설정하는 법

```java
import org.springframework.context.annotation.Bean;

@Bean
public ServletWebServerFactory serverFactory(){
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    tomcat.addAddtionalTomcatConnectors(createStandardConnector());
    return tomcat;
}
private Connector createStandardConnector(){
    Connector connector = new Connector("org.apach.coyote.http11.Http11NioProtocol");
    connector.setPort(8080);
    return connector;
//    서버 포트 다르게
}
```

### Http2 적용
* 사용하는 서블릿 컨테이너 마다 다름
* ssl 적용되있어야 됨
```yaml
server.http2.enabled=true
```

### 독립적 jar
* mvn package를 하면 jar파일이 생김
  * 의존 라이브러리들이 jar파일 안에 다 들어가 있음
    * target/app/BOOT-INF/lib
    * 내장 jar파일을 읽어 들이는 파일들이 들어있다
      * org.springframework.boot.loader.jar.jarFile
        * jar로더
      * org.springframework.loader.Launcher
        * jar파일 실행