package pro.sky.star.bank.recommendation.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = {"pro.sky.star.bank.recommendation.repository"},
        entityManagerFactoryRef = "recommendationEntityManagerFactory",
        transactionManagerRef = "recommendationTransactionManager"

)
public class RecommendationDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "recommendation.datasource")
    public DataSourceProperties recommendationDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name = "recommendationDataSource")
    @Primary
    public DataSource recommendationDataSource() {
        return recommendationDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "recommendationEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean recommendationEntityManagerFactory(
            @Qualifier("recommendationDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(dataSource)
                .packages("pro.sky.star.bank.recommendation.model")
                .build();
    }

    @Bean(name = "recommendationTransactionManager")
    @Primary
    public PlatformTransactionManager recommendationTransactionManager(
            @Qualifier("recommendationEntityManagerFactory") LocalContainerEntityManagerFactoryBean recommendationEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(recommendationEntityManagerFactory.getObject()));
    }

    @Bean(name = "recommendationJdbcTemplate")
    @Primary
    public JdbcTemplate recommendationJdbcTemplate(
            @Qualifier("recommendationDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }

}