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
        basePackages = "com.task.tasktest.repository", // Repository package for dbB
        entityManagerFactoryRef = "dbBEntityManagerFactory",
        transactionManagerRef = "dbBTransactionManager"
)
public class DbBConfig {

    @Bean(name = "dbBDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-b")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dbBEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dbBDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.task.tasktest.model") // Entity package for dbB
                .persistenceUnit("dbB")
                .build();
    }

    @Bean(name = "dbBTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("dbBEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
