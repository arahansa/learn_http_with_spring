package com.arahansa.config;


import com.arahansa.extended.CustomEncodedResourceResolver;
import com.arahansa.extended.CustomPathResourceResolver;
import com.arahansa.support.TestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new CustomEncodedResourceResolver())
                .addResolver(new CustomPathResourceResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("뉴 인터셉터?!");
        registry.addInterceptor(new TestInterceptor()).addPathPatterns("/**");
    }
}
