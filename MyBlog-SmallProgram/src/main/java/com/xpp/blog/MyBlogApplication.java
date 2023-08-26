package com.xpp.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xpp.blog.mapper")
public class MyBlogApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(MyBlogApplication.class, args);
	
	}

}
