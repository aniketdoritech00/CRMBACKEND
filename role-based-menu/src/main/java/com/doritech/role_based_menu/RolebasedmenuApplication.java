package com.doritech.role_based_menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RolebasedmenuApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RolebasedmenuApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RolebasedmenuApplication.class);
	}
}
