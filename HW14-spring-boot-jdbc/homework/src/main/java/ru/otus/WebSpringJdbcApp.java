package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*

    // Клиенты
    http://localhost:8080/client/all
    http://localhost:8080/client
*/
@SpringBootApplication
public class WebSpringJdbcApp {

    public static void main(String[] args) {
        SpringApplication.run(WebSpringJdbcApp.class, args);
    }
}
