package com.dotsub.webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.dotsub.webapp.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {
}
