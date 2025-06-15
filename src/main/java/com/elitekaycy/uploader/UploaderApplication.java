package com.elitekaycy.uploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UploaderApplication {

  public static void main(String[] args) {
    SpringApplication.run(UploaderApplication.class, args);
  }
}
