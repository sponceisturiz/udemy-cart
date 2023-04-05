package mx.com.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import mx.com.course.config.SwaggerConfig;

@SpringBootApplication
@ComponentScan(
    basePackageClasses = SwaggerConfig.class,
    basePackages = {
    "mx.com.course.controller",
    "mx.com.course.exception",
    "mx.com.course.mapper",
    "mx.com.course.model",
    "mx.com.course.repository",
    "mx.com.course.service"
})
public class TestConfiguration {
    public static void main(String[] args){
        SpringApplication.run(TestConfiguration.class, args);
    }
}
