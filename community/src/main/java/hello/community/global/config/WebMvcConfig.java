package hello.community.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		registry.addResourceHandler("/tmp/**")
				.addResourceLocations("file:///E:/Development/eclipse-workspace/storage/tmp/");
		
		registry.addResourceHandler("/file/**")
				.addResourceLocations("file:///E:/Development/eclipse-workspace/storage/file/");
		
		registry.addResourceHandler("/**")
        		.addResourceLocations("classpath:/templates/", "classpath:/static/");
	}

	
}
