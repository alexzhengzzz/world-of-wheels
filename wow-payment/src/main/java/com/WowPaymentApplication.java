package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan("com.mapper")
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class WowPaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(WowPaymentApplication.class, args);
    }
}
