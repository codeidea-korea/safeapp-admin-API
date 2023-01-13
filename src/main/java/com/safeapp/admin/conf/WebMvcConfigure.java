package com.safeapp.admin.conf;

import java.util.List;

import com.safeapp.admin.conf.handler.BfPageableHandlerInArgument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {
	@Bean
	public BfPageableHandlerInArgument bfPageableResolver() {
		return new BfPageableHandlerInArgument();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(bfPageableResolver());
	}

}