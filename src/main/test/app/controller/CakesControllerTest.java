package app.controller;

import app.dto.CakeDto;
import app.entity.Status;
import app.repository.CakeFilter;
import app.service.CakeServiceImpl;
import app.utils.Utils;
import app.view.CakeView;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//
public class CakesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CakeServiceImpl service;

    @InjectMocks
    private CakesController controller;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void getCakeViewPage() {
        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setId(1L);
        cakeDto1.setName("1-testCakeDto");
        cakeDto1.setStatus(Status.FRESH);


        CakeDto cakeDto2 = new CakeDto();
        cakeDto2.setId(2L);
        cakeDto2.setName("2-testCakeDto");
        cakeDto2.setStatus(Status.FRESH);

        CakeDto cakeDto3 = new CakeDto();
        cakeDto3.setId(3L);
        cakeDto3.setName("3-testCakeDto");
        cakeDto3.setStatus(Status.FRESH);

        ArrayList<CakeDto> cakeDtos = new ArrayList<>(Arrays.asList(cakeDto1, cakeDto2, cakeDto3));

        CakeView cakeView = new CakeView.Builder().setItems(cakeDtos).setTotal((long) cakeDtos.size()).build();

        when(service.getView(any(CakeFilter.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> cakeView));

        try {
            MvcResult result = mockMvc.perform(get("/cakes")).andExpect(status().isOk()).andReturn();
            String contentAsString = result.getResponse().getContentAsString();
            CakeView gettedView = Utils.getObjectFromJson(new Gson(), contentAsString, new CakeView());

            gettedView.getItems().forEach(cakeDto ->
                    TestCase.assertTrue(cakeView.getItems().contains(cakeDto)));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getCakeById_item_id_1() {

        CakeDto cakeDto = new CakeDto();
        cakeDto.setName("@@WebCakeDto");
        cakeDto.setStatus(Status.FRESH);
        cakeDto.setId(1L);

        when(service.getItem(1L)).thenReturn(
                CompletableFuture.supplyAsync(() -> cakeDto));

        try {

            MvcResult result = mockMvc.perform(get("/cakes/1"))
                    .andExpect(status().isOk()).andReturn();

            CakeDto objectFromJson = Utils.getObjectFromJson(new Gson(),
                    result.getResponse().getContentAsString()
                    , new CakeDto());

            TestCase.assertEquals(objectFromJson.getId(), cakeDto.getId());
            TestCase.assertEquals(objectFromJson.getName(), cakeDto.getName());
            TestCase.assertEquals(objectFromJson.getStatus(), cakeDto.getStatus());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCakeById_null() {

        when(service.getItem(0L)).thenReturn(
                CompletableFuture.supplyAsync(() -> null));

        try {
            MvcResult result = mockMvc.perform(get("/cakes/0")).andExpect(status().isNotFound()).andReturn();

            TestCase.assertEquals("{\"msg\" : \"cake not found\"}", result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void saveCake_bad_data() {

        try {
            MvcResult test = mockMvc.perform(post("/cakes/")
                    .content("{\"name\": \"name\"}")
                    .contentType("application/json"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
            System.out.println(test.getResponse().getContentAsString());
            System.out.println(test.getResponse().getStatus());

            TestCase.assertEquals("{\"msg\" : \"bad data\"}", test.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveCake_good_data() {

        List<CakeDto> cakeDtoList = new ArrayList<>();

        CakeDto cakeDto = new CakeDto();
        cakeDto.setName("name");
        cakeDto.setStatus(Status.FRESH);

        when(service.saveItem(cakeDto)).thenReturn(
                CompletableFuture.runAsync(() ->
                        cakeDtoList.add(cakeDto)));

        try {
            MvcResult test = mockMvc.perform(post("/cakes/")
                    .content("{\"name\": \"name\", \"status\": \"FRESH\"}")
                    .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andReturn();

            TestCase.assertEquals("{\"msg\" : \"ok\"}", test.getResponse().getContentAsString());

            TestCase.assertTrue(cakeDtoList.contains(cakeDto));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteCakeById() {
        List<CakeDto> cakeDtoList = new ArrayList<>();

        CakeDto cakeDto = new CakeDto();
        cakeDto.setName("name");
        cakeDto.setStatus(Status.FRESH);
        cakeDto.setId(1L);

        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setName("name");
        cakeDto1.setStatus(Status.FRESH);
        cakeDto1.setId(2L);


        cakeDtoList.add(cakeDto);
        cakeDtoList.add(cakeDto);

        when(service.removeItem(1L)).thenReturn(

                CompletableFuture.supplyAsync(() ->
                        {
                            for (CakeDto ca : cakeDtoList) {
                                 if(ca.getId().equals(1L))
                                     return ca;
                            }
                            return null;
                        })
                .thenCompose(cake -> CompletableFuture
                        .runAsync(()-> cakeDtoList.remove(cake))));


        try {

            MvcResult result1 = mockMvc.perform(delete("/cakes/1"))
                    .andExpect(status().isOk()).andReturn();


            TestCase.assertEquals(
                    "{\"msg\" : \"cake successful deleted\"}"
                    , result1.getResponse().getContentAsString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}