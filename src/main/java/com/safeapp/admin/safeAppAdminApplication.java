package com.safeapp.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@PropertySource(value = {"classpath:/application-local.properties"})
@EnableJpaAuditing
@MapperScan("com.safeapp.admin.mapper")
public class safeAppAdminApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {

        return builder.sources(safeAppAdminApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(safeAppAdminApplication.class, args);
    }

}