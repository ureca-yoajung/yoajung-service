package com.ureca.yoajungserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class YoajungServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoajungServerApplication.class, args);
    }

}
