package com.task.tasktest.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.task.tasktest.repository.dbA", // Repository package for dbA
        entityManagerFactoryRef = "dbAEntityManagerFactory",
        transactionManagerRef = "dbATransactionManager"
)
public class DbAConfig {

    @Primary
    @Bean(name = "dbADataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db-a")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dbAEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dbADataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.task.tasktest.model") // Entity package for dbA
                .persistenceUnit("dbA")
                .build();
    }

    @Primary
    @Bean(name = "dbATransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("dbAEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
