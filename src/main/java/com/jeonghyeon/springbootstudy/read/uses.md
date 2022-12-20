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