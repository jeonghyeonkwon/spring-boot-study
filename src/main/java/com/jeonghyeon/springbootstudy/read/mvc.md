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

