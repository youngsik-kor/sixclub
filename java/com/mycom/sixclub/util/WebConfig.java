package com.mycom.sixclub.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:8080")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowCredentials(true).maxAge(3600);
	}

	public WebConfig() {
		System.out.println("✅ WebConfig 초기화됨");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("Upload dir = " + uploadDir);
		registry.addResourceHandler("/equip/**").addResourceLocations("file:///" + uploadDir + "/equip/")
				.resourceChain(true).addResolver(new PathResourceResolver() {
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException {
						Resource resolved = super.getResource(resourcePath, location);
						System.out.println("요청 파일: " + resourcePath + " → 실제 파일: "
								+ (resolved != null ? resolved.getFile() : "없음"));
						return resolved;
					}
				});
	}

}