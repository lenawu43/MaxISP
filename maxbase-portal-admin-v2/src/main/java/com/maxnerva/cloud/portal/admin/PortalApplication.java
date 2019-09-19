package com.maxnerva.cloud.portal.admin;




import com.maxnerva.cloud.portal.admin.service.TicketManagerUtil;
import com.spring4all.swagger.EnableSwagger2Doc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@EnableSwagger2Doc
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.maxnerva.cloud.portal.admin.dao.mapper")
public class PortalApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }

	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public RestTemplateProvider restTemplateProvider() {
//        return new RestTemplateProvider(restTemplate());
//    }

    @Bean
    public TicketManagerUtil ticketManagerUtil(){
    	return new TicketManagerUtil();
    }
	@Override
	public void run(String... arg0) throws Exception {
		
	}
	
}