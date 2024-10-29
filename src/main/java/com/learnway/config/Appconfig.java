package com.learnway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Appconfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/comFile/**")
		.addResourceLocations("file:///learway/img/studyself/");
	}

/*    @Value("${KEY_STORE_PASSWORD}")
    private String keyStorePassword;

    @Value("${KEY_STORE_ALIAS}")
    private String keyStoreAlias;*/

}
