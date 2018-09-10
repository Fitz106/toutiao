package com.jyp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:consumer.xml")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ToutiaoWebApplication  extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{
	public static void main(String[] args) {
		SpringApplication.run(ToutiaoWebApplication.class,args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ToutiaoWebApplication.class);
	}

	//@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8090 );
	}
}
