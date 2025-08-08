package com.example.ecommercespring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/").setViewName("redirect:/server/readAllProducts");
        registry.addViewController("/registra").setViewName("registra");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/homeAdmin").setViewName("homeAdmin");
        registry.addViewController("/table").setViewName("table");
        registry.addViewController("/insProd").setViewName("insProd");
        registry.addViewController("/layout").setViewName("layout");
        registry.addViewController("/server/table1").setViewName("server/table1");
        registry.addViewController("/server/deleteProd/").setViewName("server/deleteProd/");
        registry.addViewController("/server/editProd/").setViewName("server/editProd/");
        registry.addViewController("/server/table2").setViewName("server/table2");
        registry.addViewController("/tableUser").setViewName("tableUser");
        registry.addViewController("/server/banned/").setViewName("server/banned/");
        registry.addViewController("/server/setAdmin/").setViewName("server/setAdmin/");
        registry.addViewController("/server/readAllProducts").setViewName("server/readAllProducts");
        registry.addViewController("/profileUser").setViewName("profileUser");
        registry.addViewController("/cart").setViewName("cart");


    }
}