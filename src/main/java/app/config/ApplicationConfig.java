package app.config;


import app.repository.CakeRepositoryImpl;
import app.service.CakeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean(name = "cakeRepository")
    public CakeRepositoryImpl getCakeRepository() {
        return new CakeRepositoryImpl();
    }

    @Bean(name = "cakeService")
    public CakeServiceImpl getCakeService() {
        return new CakeServiceImpl();
    }

}
