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