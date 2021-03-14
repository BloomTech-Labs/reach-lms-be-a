package com.lambdaschool.oktafoundation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * Configures the default Swagger Documentation
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Config {

	/**
	 * Configures what to document using Swagger
	 *
	 * @return A Docket which is the primary interface for Swagger configuration
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.lambdaschool.oktafoundation"))
				.paths(PathSelectors.regex("/.*"))
				.build()
				.apiInfo(apiEndPointsInfo());
	}

	/**
	 * Configures some information related to the Application for Swagger
	 *
	 * @return ApiInfo a Swagger object containing identification information for this application
	 */
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Java Spring Reach LMS - API Details")
				.description("Java Spring Reach Learning Management System Backend")
				.contact(new Contact("Reach LMS Team-A - Labs 31", "http://www.lambdaschool.com", "someone@lambdaschool.com"))
				.license("MIT")
				.licenseUrl("https://github.com/LambdaSchool/java-springfoundation/blob/master/LICENSE")
				.version("1.0.0")
				.build();
	}

	// Enable LinkDiscoverers for org.springframework.hateoas.config.HateoasConfiguration
	@Primary
	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> plugins = new ArrayList<>();
		plugins.add(new HalLinkDiscoverer());
		plugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

	}

}