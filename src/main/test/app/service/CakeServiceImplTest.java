package app.service;

import app.dto.CakeDto;
import app.entity.Cake;
import app.entity.Status;
import app.repository.CakeFilter;
import app.repository.CakeRepository;
import app.utils.Utils;
import app.view.CakeView;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CakeServiceImplTest {

    @Autowired
    CakeService cakeService;

    @Autowired
    CakeRepository cakeRepository;

    @Autowired
    Cake testCake;

    @Autowired
    @Qualifier("testServiceCakeFilter")
    CakeFilter testCakeFilter;

    @Autowired
    @Qualifier("testServiceCakeDto")
    CakeDto cakeDto;

    @Autowired
    @Qualifier("testServiceSaveItemCakeDto")
    CakeDto cakeDtoSaveItem;

    @Autowired
    @Qualifier("testServiceRemoveItemCake")
    Cake cakeToRemove;

    @Autowired
    @Qualifier("testServiceCakeList")
    List<Cake> cakesList;

    @Autowired
    @Qualifier("testServiceCakeFilterForTotal")
    CakeFilter cakeFilter;

    @Test
    public void getItem() {

        Cake test = new Cake(this.cakeDto);

        Utils.execute(this.cakeRepository.addItem(test));

        Long id = test.getId();
        CakeDto data = Utils.getData(this.cakeService.getItem(id), null);

        TestCase.assertEquals(this.cakeDto.getName(), data.getName());
        TestCase.assertEquals(this.cakeDto.getStatus(), data.getStatus());

        cakeService.removeItem(id);
    }

    @Test
    public void getView() {

        Status[] statuses = this.testCakeFilter.getStatuses();
        Status status = statuses[0];

        List<Status> listStatuses = Arrays.asList(statuses);

        this.cakeDto.setStatus(status);

        Cake cakeT = new Cake(this.cakeDto);

        Utils.execute(this.cakeRepository.addItem(cakeT));

        CakeView data = Utils.getData(this.cakeService.getView(this.testCakeFilter), null);

        TestCase.assertNotNull(data);
        Long index = 0L;
        for (CakeDto el : data.getItems()) {
            index++;
            TestCase.assertTrue(listStatuses.contains(el.getStatus()));
            System.out.println(el);
        }
        TestCase.assertEquals(data.getTotal(), index);
        TestCase.assertFalse(data.getItems().isEmpty());

        Utils.execute(this.cakeRepository.removeItem(cakeT));

    }

    @Test
    public void saveItem() {

        Cake saveCake = new Cake(this.cakeDtoSaveItem);

        CakeFilter cakeFilter = new CakeFilter.Builder().setText(this.cakeDtoSaveItem.getName())
                .setStatuses(new Status[]{this.cakeDtoSaveItem.getStatus()}).build();

        List<Cake> listCakes = Utils.getData(this.cakeRepository.getRange(cakeFilter), new ArrayList<>());

        TestCase.assertTrue(listCakes.isEmpty());

        Utils.execute(this.cakeService.saveItem(this.cakeDtoSaveItem));

        listCakes = Utils.getData(this.cakeRepository.getRange(cakeFilter), new ArrayList<>());

        TestCase.assertTrue(!listCakes.isEmpty());

        listCakes.forEach(el -> Utils.execute(this.cakeRepository.removeItem(el)));


    }

    @Test
    public void removeItem() {

        Utils.execute(this.cakeRepository.addItem(this.cakeToRemove));

        Long id = this.cakeToRemove.getId();

        Utils.execute(this.cakeService.removeItem(id));

        Cake data = Utils.getData(this.cakeRepository.getItem(id), null);

        TestCase.assertNotSame(this.cakeToRemove, data);

    }

    @Test
    public void getTotal() {

        this.cakesList.forEach(cake ->
            Utils.execute(this.cakeRepository.addItem(cake))
        );

        Long total = Utils.getData(this.cakeRepository.getTotal(this.cakeFilter), 0L);

        System.out.println("Current total : " + this.cakesList.size());
        System.out.println("Repository total : " + total);

        TestCase.assertEquals(total,new Long(this.cakesList.size()));

        this.cakesList.forEach(cake -> Utils.execute(this.cakeRepository.removeItem(cake)));

    }

}