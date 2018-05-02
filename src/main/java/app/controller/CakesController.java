package app.controller;

import app.dto.CakeDto;
import app.repository.CakeFilter;
import app.service.CakeService;
import app.utils.Utils;
import app.view.CakeView;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(path = "/cakes")
public class CakesController {


    @Autowired
    @Qualifier("cakeService")
    CakeService service;


    @RequestMapping(path = "", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CakeView getCakeViewPage(@RequestParam(name = "cakeFilter", defaultValue = "{}") String cakeFilter) {
        CakeFilter filterFromJson = Utils.getObjectFromJson(new Gson(), cakeFilter);
        return Utils.getData(service.getView(filterFromJson), new CakeView());

    }

    @RequestMapping(path = "/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getCakeById(@PathVariable Long id) {

        CakeDto cakeDto = Utils.getData(service.getItem(id), null);

        if (cakeDto == null)
            return new ResponseEntity<>("{\"msg\" : \"cake not found\"}", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(cakeDto, HttpStatus.OK);
    }

    @RequestMapping(path = "/", produces = "application/json", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> saveCake(@RequestBody CakeDto cakeDto) {
        if(cakeDto.getName() == null || cakeDto.getStatus() == null)
            return new ResponseEntity<>("{\"msg\" : \"bad data\"}", HttpStatus.BAD_REQUEST);

        CompletableFuture<Void> cf = service.saveItem(cakeDto);
        try {
            cf.get();
            return new ResponseEntity<>("{\"msg\" : \"ok\"}", HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
             return new ResponseEntity<>("{\"msg\" : \"data not saved\"}", HttpStatus.BAD_REQUEST);
        }


    }


    @RequestMapping(path = "/{id}",produces = "application/json", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<String> deleteCakeById(@PathVariable Long id){

        try {
            CompletableFuture<Void> cf = service.removeItem(id);
            cf.get();
            return new ResponseEntity<>("{\"msg\" : \"cake successful deleted\"}",HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            return new ResponseEntity<>("{\"msg\" : \"cake is not deleted\"}",HttpStatus.BAD_REQUEST);
        }
    }
}
