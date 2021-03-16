package com.lambdaschool.oktafoundation.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@EnableHypermediaSupport(type = {HypermediaType.HAL})
public class HypermediaConfig {}
