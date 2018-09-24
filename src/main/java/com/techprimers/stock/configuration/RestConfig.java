package com.techprimers.stock.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * The rest template has to be created as part of the configuration, before
 * the Spring Eureka loads, because in this way we will have only one instance, 
 * and this can be under control of Eureka to handle load balancing.
 */
@Configuration
public class RestConfig {
    @LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
