package app.service;

import app.dto.CakeDto;
import app.entity.Cake;
import app.repository.CakeFilter;
import app.repository.CakeRepository;
import app.view.CakeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Class implements {@Link CakeService}
 */
public class CakeServiceImpl implements CakeService {

    @Autowired
    @Qualifier("cakeRepository")
    CakeRepository repository;

    @Override
    public CompletableFuture<CakeDto> getItem(Long id) {

        return repository.getItem(id)
                .thenCompose(cake -> CompletableFuture
                        .supplyAsync(() -> new CakeDto(cake)));
    }

    @Override
    public CompletableFuture<CakeView> getView(CakeFilter filter) {

        return repository.getRange(filter).thenCompose(cakes -> CompletableFuture
                .supplyAsync(() -> {

                    List<CakeDto> cakeDtoList = new ArrayList<>();
                    //Convert from cakes to dto cakes
                    cakes.forEach(cake -> cakeDtoList
                            .add(new CakeDto(cake)));

                    return (new CakeView.Builder())
                            .setItems(cakeDtoList)
                            .setTotal((long) cakes.size())
                            .build();
                }));

    }

    @Override
    public CompletableFuture<Void> saveItem(CakeDto item) {

        return repository.addItem(new Cake(item));
    }

    /**
     * This method must return CompletableFuture<int> to contain status
     * For example with id = 12312312... On front-end a would like to know
     * what the status of process 'removing data'  i have;
     * If object not exist get a '404' for example or smth else
     * If successful '200' e.t.c
     */
    @Override
    @Transactional
    public CompletableFuture<Void> removeItem(Long id) {


        return repository.getItem(id)
                .thenCompose(cake ->{
                    if (cake != null)
                        return CompletableFuture.runAsync(
                                () -> repository.removeItem(cake));
                    //need to return CompletableFuture
                    //
                    else return CompletableFuture.runAsync(
                            () -> System.out.println("try to delete not existed cake"));
                });
    }

    @Override
    public CompletableFuture<Long> getTotal(CakeFilter filter) {
        return repository.getTotal(filter);
    }
}
