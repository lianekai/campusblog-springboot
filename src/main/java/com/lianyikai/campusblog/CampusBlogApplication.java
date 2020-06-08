package com.lianyikai.campusblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.lianyikai.campusblog.dao", "com.lianyikai.campusblog.pojo"})
public class CampusBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusBlogApplication.class, args);
    }

}
