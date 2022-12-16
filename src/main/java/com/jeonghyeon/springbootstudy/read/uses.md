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