package app.repository;

import app.entity.Cake;
import app.entity.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of {@Link app.repository.CakeRepository} interface
 */

public class CakeRepositoryImpl implements CakeRepository {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    @Transactional
    public CompletableFuture<Cake> getItem(Long id) {

        return CompletableFuture.supplyAsync(() -> sessionFactory.openSession().get(Cake.class, id));

    }

    @Override
    @Transactional
    public CompletableFuture<List<Cake>> getRange(CakeFilter filter) {

        return CompletableFuture.supplyAsync(() -> {

            Session session = sessionFactory.openSession();

            Query<Cake> query = session.createQuery(addFilterToSession(sessionFactory.openSession(), filter));

            query = setLimits(query, filter);

            return query.getResultList();

        });
    }

    @Override
    @Transactional
    public CompletableFuture<Void> addItem(Cake item) {


        return CompletableFuture.runAsync(() ->
                sessionFactory.openSession().save(item));
    }

    @Override
    @Transactional
    public CompletableFuture<Void> updateItem(Cake item) {
        return CompletableFuture.runAsync(() -> sessionFactory.openSession().saveOrUpdate(item));
    }

    @Override
    public CompletableFuture<Void> removeItem(Cake item) {
        Session session = sessionFactory.openSession();
        return CompletableFuture.runAsync(() -> {
            session.beginTransaction();
            session.delete(item);
            session.flush();
            session.getTransaction().commit();

        });
    }

    @Override
    @Transactional
    public CompletableFuture<Long> getTotal(CakeFilter filter) {
        return getRange(filter).thenCompose(cakes ->
                CompletableFuture.supplyAsync(() -> ((Integer) cakes.size()).longValue()));
    }

    private CriteriaQuery<Cake> addFilterToSession(Session session, CakeFilter filter) {

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Cake> query = cb.createQuery(Cake.class);
        Root<Cake> root = query.from(Cake.class);


        List<Predicate> predicates = new ArrayList<>();

        if (filter.getText() != null && !filter.getText().isEmpty())
            predicates.add(cb.like(root.get("name"), "%" + filter.getText() + "%"));


        if (filter.getStatuses() != null && filter.getStatuses().length != 0) {
            Expression<Status> expressionStatus = root.get("status");
            predicates.add(expressionStatus.in(Arrays.asList(filter.getStatuses())));
        }

        Predicate andPredicate = null;

        if (predicates.size() > 1)
            andPredicate = cb.and(predicates.toArray(new Predicate[predicates.size()]));
        else if (predicates.size() == 1)
            andPredicate = predicates.get(0);

        if (andPredicate != null)
            query.where(andPredicate);

        query.select(root);

        return query;
    }

    private Query<Cake> setLimits(Query<Cake> query, CakeFilter filter) {

        final int EMPTY_LIMIT = 0;
        final int EMPTY_PAGE = 0;
        final int FIRST_RESULT = 1;
        final int PREVIOUSLY_PAGE = 1;

        int limit = filter.getLimit();
        int page = filter.getPage();
        // Если лимит страницы , номер страницы  заполнены
        // Тогда начальный результат будет равен page * limit;
        // Если указан лимит страницы тогда начиная с 1-ой страницы
        // По лими на странице будет выдан результат
        if (limit != EMPTY_LIMIT && page != EMPTY_PAGE) {
            query.setFirstResult((page - PREVIOUSLY_PAGE) * limit);
            query.setMaxResults(limit);
        } else if (page == EMPTY_PAGE && limit != EMPTY_LIMIT) {
            query.setFirstResult(FIRST_RESULT);
            query.setMaxResults(limit);
        }


        return query;
    }
}
