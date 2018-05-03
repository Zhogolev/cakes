package app.config;

import app.entity.Cake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@EnableJpaRepositories
public class RepositoryConfig {

    @Autowired
    private Environment env;


    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean getSessionMyFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setPackagesToScan("app.entity");
        factoryBean.setDataSource(getDataSource());
        factoryBean.setAnnotatedClasses(Cake.class);
        factoryBean.setHibernateProperties(getProps());

        return factoryBean;

    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource());
        em.setPackagesToScan("app.entity");
        em.setJpaProperties(getProps());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.user"));
        dataSource.setPassword(env.getProperty("db.pass"));

        return dataSource;
    }

    @Bean
    public Properties getProps() {
        Properties props = new Properties();

        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.c3p0.min_size", "10");
        props.setProperty("hibernate.c3p0.max_size", "100");
        props.setProperty("hibernate.c3p0.acquire_increment", "1");
        props.setProperty("hibernate.c3p0.timeout", "2000");
        props.setProperty("hibernate.c3p0.max_statements", "150");
        props.setProperty("javax.persistence.schema-generation.database.action", env.getProperty("data.action"));


        return props;
    }

}
