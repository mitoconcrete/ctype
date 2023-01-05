package com.sparta.posting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaMidProjectCApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartaMidProjectCApplication.class, args);
    }

}
