package com.missionpossible.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; //comment out when you have a DB ready

// comment in when you have a DB ready
//@SpringBootApplication
//public class BackendApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(BackendApplication.class, args);
//    }
//
//}

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

//NOTE
//Check the secruity config file as its disabled so its allowing all requests

//look over cors config also since it allows localhost:5173 but hwo do i allow more for production