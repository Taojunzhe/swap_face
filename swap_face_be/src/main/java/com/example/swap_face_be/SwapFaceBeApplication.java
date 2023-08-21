package com.example.swap_face_be;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.swap_face_be.dao")
public class SwapFaceBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwapFaceBeApplication.class, args);
    }

}
