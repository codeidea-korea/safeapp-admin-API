package com.binoofactory.cornsqure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan
@EnableJpaAuditing
@MapperScan("com.binoofactory.cornsqure.mapper")
@PropertySource(value = {"classpath:/application-local.properties"})
public class CornsqureApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CornsqureApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CornsqureApplication.class, args);
    }

}
