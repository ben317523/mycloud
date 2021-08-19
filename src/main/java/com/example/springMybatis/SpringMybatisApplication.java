package com.example.springMybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableScheduling
//@MapperScan("com.example.springMybatis.mapper")
public class SpringMybatisApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMybatisApplication.class, args);
	}

}
