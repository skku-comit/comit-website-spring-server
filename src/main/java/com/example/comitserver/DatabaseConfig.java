package com.example.comitserver;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.load();

        logger.info("Database URL: " + dotenv.get("DATABASE_URL"));
        logger.info("Database Username: " + dotenv.get("DATABASE_USERNAME"));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(dotenv.get("DATABASE_URL"));
        dataSource.setUsername(dotenv.get("DATABASE_USERNAME"));
        dataSource.setPassword(dotenv.get("DATABASE_PASSWORD"));

        return dataSource;
    }
}