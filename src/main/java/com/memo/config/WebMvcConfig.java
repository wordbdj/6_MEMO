package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.common.FileManagerService;
import com.memo.interceptor.PermissionInterceptor;

@Configuration // 설정을 위한 Spring bean
public class WebMvcConfig implements WebMvcConfigurer {

	// 이미지 path와 서버에 업로드 된 실제 이미지와 매핑 설정
	
	@Autowired
	private PermissionInterceptor interceptor;
	
	// 인터셉터 설정
	@Override 
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(interceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/error", "/css/**","/img/**", "/user/sign-out"); // /img -> static 밑에 있는 img
		
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/images/**") // web path    http://localhost/images/aaaa_17348493489/sun.png
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH); // 실제 이미지 파일 위치
		
	}
	
}
