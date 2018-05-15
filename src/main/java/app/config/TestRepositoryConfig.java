package app.config;

import app.dto.CakeDto;
import app.entity.Cake;
import app.entity.Status;
import app.repository.CakeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Lazy
public class TestRepositoryConfig {

    @Bean("testCakeFilterStatuses")
    public Status[] getTestStatuses() {
        return new Status[]{Status.FRESH};
    }


    @Bean("testDeleteCakeRepository")
    public Cake getCakeForDelete(@Autowired @Qualifier("testCakeDto") CakeDto cakeDto) {
        CakeDto cakeDtoDelete = new CakeDto();
        cakeDtoDelete.setName("@@@@delete");
        cakeDtoDelete.setStatus(Status.FRESH);
        return new Cake(cakeDtoDelete);
    }

    @Bean("testCake")
    public Cake getTestCake(@Autowired @Qualifier("testCakeDto") CakeDto cakeDto) {
        return new Cake(cakeDto);
    }

    @Bean(name = "testCakeName")
    public String getCakeName() {
        return "TESTNAME" + Math.round(Math.random() * 10000000);
    }

    @Bean(name = "testCakeStatus")
    public Status getCakeStatus() {
        return Status.STALE;
    }

    @Bean(name = "testCakeDto")
    public CakeDto getCakeDto(@Autowired @Qualifier("testCakeName") String cakeName,
                              @Autowired @Qualifier("testCakeStatus") Status cakeStatus) {
        CakeDto cakeDto = new CakeDto();
        cakeDto.setStatus(cakeStatus);
        cakeDto.setName(cakeName);
        return cakeDto;
    }

    @Bean(name = "testCakeFilter")
    public CakeFilter getCakeFilter(@Autowired Status[] statuses,
                                    @Autowired @Qualifier("testCakeName") String cakeName) {
        return new CakeFilter.Builder().setStatuses(statuses).setText(cakeName).build();
    }

    @Bean(name = "getTotalFilter")
    public CakeFilter getTotalFilter(){
        return new CakeFilter.Builder().setText("@@@@").build();
    }

    @Bean(name = "getTotalCakesList")
    public List<Cake> getCakesList() {

        List<Cake> cakes = new ArrayList<>();

        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setName("@@@@" + Math.round(Math.random() * 1000));
        cakeDto1.setStatus(Status.STALE);

        CakeDto cakeDto2 = new CakeDto();
        cakeDto2.setName("@@@@" + Math.round(Math.random() * 1000));
        cakeDto2.setStatus(Status.FRESH);

        CakeDto cakeDto3 = new CakeDto();
        cakeDto3.setName("@@@@" + Math.round(Math.random() * 1000));
        cakeDto3.setStatus(Status.FRESH);

        cakes.add(new Cake(cakeDto1));
        cakes.add(new Cake(cakeDto2));
        cakes.add(new Cake(cakeDto3));


        return cakes;
    }
}
