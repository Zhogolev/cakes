package app.config;

import app.entity.Cake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan("app.repository")
public class RepositoryConfig {

    @Autowired
    private Environment env;


    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setDataSource(getDataSource());
        factoryBean.setAnnotatedClasses(Cake.class);
        factoryBean.setHibernateProperties(getProps());

        return factoryBean;

    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource  dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/dom.ru");
        dataSource.setUsername("postgres");

        return dataSource;
    }

    @Bean
    public Properties getProps() {
        Properties props = new Properties();

        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.hbm2ddl.auto", "validate");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.c3p0.min_size","10");
        props.setProperty("hibernate.c3p0.max_size","100");
        props.setProperty("hibernate.c3p0.acquire_increment","1");
        props.setProperty("hibernate.c3p0.timeout","2000");
        props.setProperty("hibernate.c3p0.max_statements","150");
        return props;
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }
}
