package com.task.tasktest.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.task.tasktest.repository.dbD", // Repository package for dbB
        entityManagerFactoryRef = "dbDEntityManagerFactory",
        transactionManagerRef = "dbDTransactionManager"
)
public class DbDConfig {

    @Bean(name = "dbDDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-d")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbDEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dbDDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.task.tasktest.model") // Entity package for dbB
                .persistenceUnit("dbD")
                .build();
    }

    @Bean(name = "dbDTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("dbDEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
