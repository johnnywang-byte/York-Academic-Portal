package com.yorku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan // 扫描过滤器
@SpringBootApplication
public class WebManagemenApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebManagemenApplication.class, args);
	}

}
