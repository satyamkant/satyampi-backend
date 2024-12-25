package uk.satyampi.SecurityMs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SecurityMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityMsApplication.class, args);
	}
	@Bean
	@LoadBalanced // Enables service discovery for RestTemplate
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
