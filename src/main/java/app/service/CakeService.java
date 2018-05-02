package app.service;

import app.dto.CakeDto;
import app.entity.Cake;
import app.repository.CakeFilter;
import app.view.CakeView;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Interface contains methods to work with {@Link CakeRepository}
 */
@Service
public interface CakeService {

    /**
     * Get dto cake from repository
     *
     * @param id element id
     * @return
     */
    CompletableFuture<CakeDto> getItem(Long id);

    /**
     * @param filter filter options {@Link CakeFilter}
     * @return
     */
    CompletableFuture<CakeView> getView(CakeFilter filter);

    /**
     * Save item into repository
     * @param item Cake
     * @return
     */
    CompletableFuture<Void> saveItem(CakeDto item);

    /**
     * Remove item from repository
     * @param id remove item id
     * @return
     */
    CompletableFuture<Void> removeItem(Long id);

    /**
     * Get total of cakes sorting by filter
     *
     * @param filter filter options {@Link CakeFilter}
     * @return
     */
    CompletableFuture<Long> getTotal(CakeFilter filter);
}