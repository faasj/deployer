package com.faasj.deployer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class DeployerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeployerApplication.class, args);
    }

}
