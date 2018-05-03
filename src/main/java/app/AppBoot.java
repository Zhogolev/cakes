package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans({
        @ComponentScan("app.config"),
        @ComponentScan("app.controller"),
        @ComponentScan("app.service"),
        @ComponentScan("app.repository")})
@EnableAutoConfiguration
public class AppBoot {
    public static void main(String[] args) {
        SpringApplication.run(AppBoot.class, args);
    }
}
