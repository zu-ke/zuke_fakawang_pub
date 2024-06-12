package com.zk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.zk.mapper"})
public class FaKaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaKaApplication.class, args);

    }
}
