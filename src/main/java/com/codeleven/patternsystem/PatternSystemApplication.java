package com.codeleven.patternsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@MapperScan("com.codeleven.patternsystem.dao")
public class PatternSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatternSystemApplication.class, args);
    }

}
