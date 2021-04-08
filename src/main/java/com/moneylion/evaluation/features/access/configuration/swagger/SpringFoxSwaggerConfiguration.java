package com.moneylion.evaluation.features.access.configuration.swagger;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxSwaggerConfiguration {

	@Bean
	public Docket getSwaggerConfigurationDocket() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/feature"))
				.apis(RequestHandlerSelectors.basePackage("com.moneylion")).build().apiInfo(getAPIInfo())
				.useDefaultResponseMessages(false);
	}

	private ApiInfo getAPIInfo() {
		return new ApiInfo("Features Access Control API",
				"API to add/enable/disable feature access of users (by their email).", "1.0",
				"https://github.com/shubhamsharma1304/moneylion-assessment/blob/master/README.md",
				new Contact("Shubham Sharma",
						"https://github.com/shubhamsharma1304/moneylion-assessment/blob/master/README.md",
						"shubham.sharma1304@gmail.com"),
				"N/A - Free use", "https://github.com/shubhamsharma1304/moneylion-assessment/blob/master/README.md",
				Collections.emptySet());
	}
}