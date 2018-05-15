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
public class TestServiceConfig {

    @Bean(name = "totalItemsName")
    public String getTotalNames(){
        return "@@@total";
    }


    @Bean(name = "testServiceCakeDto")
    public CakeDto getTestCakeDto() {
        CakeDto cakeDto = new CakeDto();
        cakeDto.setStatus(Status.FRESH);
        cakeDto.setName("@@@testServiceCake");
        return cakeDto;
    }

    @Bean(name = "testServiceSaveItemCakeDto")
    public CakeDto getTestSaveItemCakeDto() {
        CakeDto cakeDto = new CakeDto();
        cakeDto.setStatus(Status.FRESH);
        cakeDto.setName("@@@SaveItem");
        return cakeDto;
    }

    @Bean(name = "testServiceCakeFilter")
    public CakeFilter getCakeFilter(){
        return new CakeFilter.Builder().setStatuses(new Status[]{Status.FRESH}).build();
    }

    @Bean(name = "testServiceRemoveItemCake")
    public Cake getCakeForRemoveItemService(){
        CakeDto cakeDto = new CakeDto();
        cakeDto.setName("@@@RemoveItem");
        cakeDto.setStatus(Status.STALE);
        return new Cake(cakeDto);
    }

    @Bean(name = "testServiceCakeList")
    public List<Cake> getCakeList(@Autowired @Qualifier("totalItemsName") String name){

        List<Cake> result = new ArrayList<>();

        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setName(name);


        result.add(new Cake(cakeDto1));
        result.add(new Cake(cakeDto1));
        result.add(new Cake(cakeDto1));
        result.add(new Cake(cakeDto1));
        result.add(new Cake(cakeDto1));

        return result;
    }

    @Bean(name = "testServiceCakeFilterForTotal")
    public CakeFilter getCakeFilterForTotal(@Autowired @Qualifier("totalItemsName") String name){
        return new CakeFilter.Builder().setText(name).build();
    }

}
