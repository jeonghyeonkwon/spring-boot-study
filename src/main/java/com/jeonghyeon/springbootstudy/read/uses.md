# 스프링 부트 활용

## SpringApplication

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
//       ==
        SpringApplication application = new SpringApplication(BootApplication.class);
        application.run(args);
//       ==
        new SpringApplicationBuilder()
                .sources(BootApplication.class)
                .run(args);
    }
}
```

### Event Listener 만들기

```java
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println("========================");
        System.out.println("Application is starting");
        System.out.println("========================");
//        이렇게 하면 작동하지않는다
    }
}
```
* 등록하기(위에 Listener에 @Component 주석)

```java
import com.jeonghyeon.springbootstudy.SimpleListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringInitApplication {
    public static void main(Sttring[] args) {
        SpringApplication app = new SpringApplication(SpringInitApplication.class);
        app.addListeners(new SimpleListener());
        app.run(args);
    }
}
```


* ApplicationListener<ApplicationStartingEvent>
  * ApplicationContext 만들어지기 이전임
  * 직접 등록해야됨
* ApplicationListener<ApplicationStartedEvent>
  * ApplicationContext 시작 된 이후

### argument option

```java
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class SampleListener {
    public SampleListener(ApplicationArguments args) {
        System.out.println("option : " + args.containsOption("bar"));
    }
}
```
* VMOption
  * JVM OPTION 
  * -Dfoo
* Program arguments 
  * --bar
  * 이게 위 클래스이 argument임

### 애플리케이션이 실행한 뒤 뭔가 실행하고 싶을 떄
* ApplicationRunner OR CommandLineRunner

```java
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import org.springframework.boot.ApplicationRunner;

@Component
public class Sample implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ...
    }
}

@Component
public class Sample2 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        ...
    }
}
```


## 외부 설정
* application.yml OR application.properties
* 환경 변수
* 커맨드 라인 아규먼트

### 우선순위(내가 사용 할 것만 정리)
1. TestPropertySource

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@RunWith(SpringRunner.class)
// /src/test/resources.test.properties
@TestPropertySource(locations = "classpath:/test.properties")
@SpringBootTest
public class SpringInitApplicationTest {
  @Autowired
  Environment env;
  
  @Test
  public void contextLoads(){
      assertThat(env.getProperty("..."))
  }
}
```
2. 커맨드 라인 아규먼트
3. application.yml

### application.yml 우선순위(높은게 밑에것을 덮어씀)
1. file:./config/
2. file:./
3. classpath:/config/
4. classpath:/

### properties 객체로 쓰기
* pom.xml 의존성 추가
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-configuration-processor</artifactId>
  <optional>true</optional>
</dependency> 
```
* application.properties 추가
```properties
person.name=jeonghyeon
person.age=29
```
* class 만들기

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("person")
@Validated
public class Human {

    
  //    검증도 가능
  @NotEmpty
  private String notNullStr;

  private String name;

  private String age;

  public String getName() {
    return name;
  }

  public String getAge() {
    return age;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(String age) {
    this.age = age;
  }
}
```
* 사용시

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AController {
  @Autowired
  private Human human;
}

```

## 프로파일
* @Configuration, @Component에 사용한다
* application.properties에 프로파일에 준 이름을 넣는다
```properties
spring.profiles.active = prod
```

* 프로퍼티에서 다른 프로퍼티를 추가 할 수 있다
```properties
spring.profiles.include=proddb

#다른 application-proddb.properties 파일의 설정을 추가 한다
```

## 로깅
* 로깅 퍼사드
  * 실제 로거는 아니지만 여러 로거를 바꿔 사용할 수 있는 추상화된 인터페이스
* 로거
  * slf4j
  * JUL
  * Log4J2
  * Logback
* 스프링 푸트 의존 관게를 보면 밑에 처럼 되어있다 결국 log4j는 logback을 바라보게 되어있다(스프링 부트 기본 설정이...) 
  * jul -> slf4j
  * log4j -> slf4j
* 스프링 부트 로깅 
  * -Ddebug or --debug
  * --trace(전부 다 디버깅)
  * 컬러 : spring.output.ansi.enabled
  * 파일 출력 : logging.file or logging.path
    * 파일 설정(application.properties)
      * logging.file=
    * 디렉토리 설정(application.properties)
      * logging.path=
  * 로그 레벨 조정 : logging.level.패키지 = 로그 레벨  
  
## 커스텀 로그 사용하기
* Logback
  * logback-spring.xml
* Log4J2
  * log4j2-spring.xml
* JUL
  * logging.properties

### logback-spring.xml 커스텀

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <include resource="org/springframework/boot/logging/logback/base.xml"/>
  <logger name="com.jeonghyeon.springbootstudy" level="DEBUG"/>
</configuration>
```
### Logback 에서 Log4J로 바꾸기

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
    <exclusion>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
    </exclusion>
  </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

## 테스트
* 기본적인 스프링 부트 starter-test을 추가하면 오면 많은것을 사용할 수 있다.
  * 대표적으로
    * Junit
    * Spring-test
    * Mockito

### 최신 버전에서 MockMvc 만드는 방법

#### 웹 환경 변수가 MOCK 일 때(기본이 MOCK)
```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SampleControllerTest{
  
  @Autowired
  MockMvc mockMvc;
  
  @Test
  public void hello() throws Exception{
      mockMvc.perform(get("/hello"))
              .andExpect(status().isOk())
              .andExpect(content().string("return 내용"))
              .andDo(print());
  }
  
}
```
#### 웹 환경 변수가 RANDOM_PORT 일 때
* 이때는 서블릿이 뜸(내장 톰캣 사용)
```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SampleControllerTest{
  
  @Autowired
  TestRestTemplate testRestTemplate;
  
  @MockBean
  SampleService mockSampleService;
  
  @Test
  public void hello() throws Exception{
      when(mockSampleService.getName()).thenReturn("jeonghyeon");
      
      String result = testRestTemplate.getForObject("/", String.class);
      assetThat(result).isEquals("hello jeonghyeon");
  }
//  OR 
// webflux 의존성 추가하고 WebTestClient 사용하기
// asynchronus 기반
  @Autowired
  WebTestClient webTestClient;
  
  @MockBean
  SampleService mockSampleService;

  @Test
  public void hello() throws Exception{
    when(mockSampleService.getName()).thenReturn("jeonghyeon");

    webTestClient.get().uri("/api").exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("hello jeonghyeon");
  }
}
```
### MockBean
* 통합 슬라이스 테스트 두 쪽다 씀
### 슬라이스 테스트
* 위에 것은 통합 테스트
* 레이어 별로 잘라서 테스트 할 때
* @JsonTest
* @WebMvcTest
* @WebFluxTest
* @DataJpaTest
* ...

### outputcapture
* 로거 또는 System.out.println으로 다른 컨트롤러가 찍힌 것들을 테스트 할 수 있다.
```java
@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
public SampleControllerTest{
    
    @Rule
    public OutputCapture outputCapture = new OutputCapture();
    
    @Test
    public void test(){
        when(mockSampleService.getName()).thenReturn("jeonghyeon");

        webTestClient.get().uri("/api").exchange()
        .expectStatus().isOk()
        .expectBody(String.class).isEqualTo("hello jeonghyeon");
        
        assertThat(outputCapture.toString).contains("로거 내용");
    }
}

```