package com.example.ecommercespring;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication(exclude= {SecurityAutoConfiguration.class})
public class EcommercespringApplication {
	private static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		context = SpringApplication.run(EcommercespringApplication.class, args);

		System.out.println("_________________________________________________________________________________________________ ");
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

	public static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);
		
		Thread thread = new Thread(()-> {
			context.close();
			context = SpringApplication.run(EcommercespringApplication.class, args.getSourceArgs());
		});
		thread.setDaemon(false);
        thread.start();
	}

}
