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

## 정적 리소스
### 기본 경로
* classpath:/static
* classpath:/public
* classpath:/resources/
* classpath:/META-INF/resources

#### 새로운 경로를 추가하는 방법
1. application.yml에 spring.mvc.static-locations 로 추가
  * 이 방법은 기존의 경로들을 다 무시함
2. WebMvcConfigurer의 addResourceHandlers로 추가

```java

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/m/**")
                .addResourceLocations("classpath:/m/") // /로 끝나야됨
                .setCachePeriod(10);
  }
}

```
### 캐싱 기능
* header 정보에 Last-Modified 정보로 판별하여 새로운 리소스를 반환할지 있는 자원을 활용할지 결정

## 웹 jar
* 리액트, 앵귤러, 뷰 같은 라이브러리를 jar로 추가해서 사용할 수 있다
* 이때까지 사용할려면 cdn 또는 static에 다운 받아서 사용 해 왔었음... 새롭게 알게 됨
### 사용 방법(jquery 예시)
1. maven 저장소에서 jquery 를 찾아 pom.xml에 추가
2. html에 jquery 추가 (/webjars/jquery/3.3.1/...)
```html

<!doctype html>
<html lang="en">
<head>
<meta charset="UTF-8">
             <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                         <meta http-equiv="X-UA-Compatible" content="ie=edge">
             <title>Document</title>
</head>
<body>


<script src="/webjars/jquery/3.3.1/dist/jquery.min.js"></script>
</body>

</html>
```
3. 버전 바뀔때 마다 3.3.1을 수정해야 되므로 버전을 생략하게 해주는 의존성 추가 하면 됨
  * webjars-locator-core 추가
```html

<!doctype html>
<html lang="en">
<head>
<meta charset="UTF-8">
             <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                         <meta http-equiv="X-UA-Compatible" content="ie=edge">
             <title>Document</title>
</head>
<body>


<script src="/webjars/jquery/dist/jquery.min.js"></script>
</body>

</html>
```

## ExceptionHandler
### MVC에서 에러 처리하는 방법
* @ControllerAdvice
  * 한 컨트롤러 내부에서 처리하는 것이 아닌 전체 혹은 설정한 패키지 단위로 Exception을 처리 할 용도로 사용
* @ExceptionHander
  * 어떤 예외가 발생 했을 때 어떤 응답 값을 반환 할 지 정하는 곳
  * Exception 내용을 파라미터로 받는다

### 스프링 부트가 제공하는 에러 페이지
* BasicErrorController
  * HTML, JSON 형식으로 반환

## Spring HATEOAS
* 현재 RestApi에서 연관된 다른 페이지로 이동할 링크를 넣어서 반환 하는 것
  * 현재 호출 api는 게시판 리스트다. 그럼 디테일 페이지 링크, 페이지네이션 링크 정보도 넣어서 반환 
  * 강의 내용을 내 방식대로 이해한 것...

```java

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
  @GetMapping("/hello")
  public Hello hello(){
      Hello hello = new Hello();
      hello.setPrefix("Hey,");
      hello.setName("Keesun");
      
      Resource<Hello> helloResource = new Resource<>(hello);
      
      helloResource.add(linkTo(methodOn(SampleController.class).hello()).withSelfRel());
      
      return helloResource;
  }
}
```


## CORS(Cross Origin Resource Sharing)
### SOP(Same Origin Policy)
* 프로토콜, 도메인, 포트번호가 모두 일치해야 되는 정책
  * 즉 하나라도 다르면 요청을 금지한다

* 즉! 이러한 SOP 정책을 풀어주는 것

### 설정하는 법
1. 컨트롤러 설정

```java
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:8090")
@GetMapping("/hello")
public String hello(){
    return "Hello";
}
```
2. WebConfig 글로벌 설정
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("http://localhost:8090");
  }
}
```

## 스프링 데이터

### 인메모리 데이터베이스
* 지원하는 인-메모리 데이터베이스
  * H2
  * HSQL
  * Derby

* spring-starter-jdbc를 의존성 등록해주면 2가지가 들어온다
  * HikariCP
  * spring-jdbc
    * 이 클래스패스가 있으면 자동으로 빈 설정을 해줌
    * DataSource
    * JdbcTemplate

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Statement;

@Component
public class H2Runner implements ApplicationRunner {
  @Autowired
  DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Override
  public void run(ApplicationArguments args) throws Exception {
    Connection connection = dataSource.getConnection();
    connection.getMetaData().getURL();
    connection.getMetaData().getUserName();

    Statement statement = connection.createStatement();
    String sql = "CREATE TABLE ...";
    
    statement.executeUpdate(sql);// JPA 자동으로 테이블 생성하는게 이런 구조 같음
    //try catch ,transaction,... 여러가지를 해야한다
    connection.close();
    
    
    // JDBC Template도 가능
    
  }
}
```
* 인메모리 db 기본 연결 정보는 EmbeddedDataSourceConfiguration에서 확인 가능하다
  * URL, username, password 설정 관련

### MYSQL
* 유료이므로 1년마다 라이센스를 사야되고 소스코드 공개 의무도 있다.(GPL 라이센스)
* mariaDB도 소스코드 공개 의무가 있다
#### DBCP
* Database Connection Pool
* 위 코드의 dataSource.getConnection() 에서 많은 일들이 일어난다
  * 미리 만들어 놓고 쓰는 개념
    * 몇 개를 만들어 놓을 것인가
    * 얼마동안 안쓰면 최소한 몇개를 제거할 것인가
    * 서버에 얼마동안 전달 못하면 에러를 던질 것인가?
    * ...
  * 성능에 핵심적인 기능

#### HikariCP
* 스프링 부트의 기본적인 CP
* autoCommit (기본 true)
  * 커밋이라고 적지 않아도 자동으로 커밋하는 설정
* connectionTimeout (기본 30초)
* maximumPoolSize (기본 10개)
  * 기본은 10개이지만 cpu 코어갯수가 넘어가면 대기이다

#### HikariCP application.yml에서 설정 가능
```yaml
spring.datasource.hikari.*
```

## 스프링 데이터 JPA 
* JPA는 하이버네이트 기술에 기반하여 만들어 졌다
  * Spring Data JPA -> JPA -> Hibernate -> DataSource

### JPA 슬라이싱 테스트
* Repository와 관련된 빈만 테스트 하는 것
  * 인메모리 DB가 의존성에 있어야 한다
```xml

...

<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>test</scope>
</dependency>
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

  @Autowired
  DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  AccountRepository accountRepository;
  
  // 위에 3개 빈으로 등록할 수 있다
}


```

### 통합 테스트
* 스프링 부트에 있는 모든 빈들이 등록된다
  * 이때는 인 메모리DB가 아니다. 그래서 test 디렉토리 안에 테스트 용 db 정보가 담긴 application.yml 파일을 따로 만든다

```java

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.datasource.url='...'")
public class AccountRepositoryTest {

  @Autowired
  DataSource dataSource;

  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  AccountRepository accountRepository;
  

}

```

### 운영용 DB 설정

```properties
# 테스트 환경설정에는 꺼주자
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.generate-ddl=false
```

### SQL 스크립트로 사용한 데이터베이스 초기화
* ddl-auto=update 시 기존 컬럼명을 변경하면 지우지 않고 그대로 나두고 새로운 컬럼을 생성함
  * 처음 개발 동안 email로 만들다가 userEmail로 변경하면 email이라는 컬럼은 나두고 userEmail이라는 컬럼이 생성됨

#### 적용 순서
* 밑에 platform은 application.properties에서 spring.datasource.platform으로 설정 가능

1. schema.sql or schema-${platform}.sql
2. data.sql or data-${platform}.sql

#### 개발시 참고
1. 처음 개발 동안은 ddl-auto=update로 두고
2. 테스트 디렉토리에서 schema.sql로 쿼리들을 정리 및 테스트 후
3. schema.sql를 복사해서 resouce에 넣는다