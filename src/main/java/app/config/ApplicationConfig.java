package app.config;


import app.repository.CakeRepository;
import app.repository.CakeRepositoryImpl;
import app.service.CakeService;
import app.service.CakeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean(name = "cakeRepository")
    public CakeRepository getCakeRepository() {
        return new CakeRepositoryImpl();
    }

    @Bean(name = "cakeService")
    public CakeService getCakeService() {
        return new CakeServiceImpl();
    }

}
