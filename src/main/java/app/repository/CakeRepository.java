package app.repository;

import app.entity.Cake;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface CakeRepository {
    /**
     * @param id is id of selected item
      *
      *  get item form db by id
     * */
    CompletableFuture<Cake> getItem(Long id);
    /**
     * @param filter obj, contain @see CakeFilter
     *
     * get range of cakes sorting by filer
     * */
    CompletableFuture<List<Cake>> getRange(CakeFilter filter);
    /**
     * @param item
     * save item in reposiory
     * */
    CompletableFuture<Void> addItem(Cake item);

    /**
     * Updating item in repository
     *
     * @param item cake
     */
    CompletableFuture<Void> updateItem(Cake item);

    /**
     * Remove item from repository
     *
     * @param item cake
     */
    CompletableFuture<Void> removeItem(Cake item);

    /**
     * Get total of cakes
     *
     * @param filter
     * @return total sum of cakes by filter
     */
    CompletableFuture<Long> getTotal(CakeFilter filter);
}
