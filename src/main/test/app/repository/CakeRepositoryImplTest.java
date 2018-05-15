package app.repository;

import app.entity.Cake;
import app.utils.Utils;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CakeRepositoryImplTest {


    @Autowired
    CakeRepository cakeRepository;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    @Qualifier("testCake")
    Cake testCake;

    @Autowired
    @Qualifier("testDeleteCakeRepository")
    Cake cakeToDelete;

    @Autowired
    @Qualifier("getTotalCakesList")
    List<Cake> cakesList;

    @Autowired
    @Qualifier("getTotalFilter")
    CakeFilter cakeFilter;

    private Session session;

    @Before
    public void setUp() throws Exception {
        if (this.sessionFactory != null && this.session == null)
            this.session = sessionFactory.openSession();

        if (this.session == null)
            throw new Exception("Cannot open db session");

    }

    @After
    public void tearDown() {

        this.session.close();

        System.out.println("CakeRepositoryImplTest.tearDown");
    }

    @Test
    public void getItem() {
        Long idTestCake;

        this.session.save(this.testCake);
        idTestCake = this.testCake.getId();

        CompletableFuture<Cake> cf = this.cakeRepository.getItem(idTestCake);
        Cake data = Utils.getData(cf, new Cake());

        TestCase.assertTrue(data.getName().equals(this.testCake.getName()));
        TestCase.assertTrue(data.getId().equals(this.testCake.getId()));
        TestCase.assertTrue(data.getStatus() == this.testCake.getStatus());

        Utils.removeItem(session, this.testCake);
    }

    @Test
    public void getRange() {
        final int MIN_AQUIRED_CAKE_NAME = 5;
        final int BEGIN_INDEX = 2;

        String name = testCake.getName();

        if (name.length() > MIN_AQUIRED_CAKE_NAME)
            name = name.substring(BEGIN_INDEX, name.length() - BEGIN_INDEX);

        this.session.save(this.testCake);
        CakeFilter cakeFilter = new CakeFilter.Builder().setText(name).build();

        System.out.println("Substring : " + name);

        CompletableFuture<List<Cake>> range = cakeRepository.getRange(cakeFilter);
        List<Cake> data = Utils.getData(range, new ArrayList<>());

        for (Cake cake : data) {
            System.out.println("Founded : " + cake.getName());
            TestCase.assertTrue(cake.getName().contains(name));
        }

        Utils.removeItem(this.session, this.testCake);
    }


    @Test
    public void addItem() {

        final String COLUMN_CAKE_NAME = "name";
        List<Cake> resultList;

        CriteriaBuilder cb = this.session.getCriteriaBuilder();
        CriteriaQuery<Cake> criteriaQuery = cb.createQuery(Cake.class);
        Root<Cake> root = criteriaQuery.from(Cake.class);
        criteriaQuery.where(cb.equal(root.get(COLUMN_CAKE_NAME), this.testCake.getName()));
        Query<Cake> query = this.session.createQuery(criteriaQuery);

        resultList = query.getResultList();

        TestCase.assertTrue(resultList.isEmpty());

        Utils.execute(this.cakeRepository.addItem(testCake));

        resultList = query.getResultList();

        TestCase.assertTrue(!resultList.isEmpty());

        resultList.forEach(el -> {
            System.out.println(el);
            TestCase.assertTrue(el.getName().equals(this.testCake.getName()));
            TestCase.assertTrue(el.getStatus().equals(this.testCake.getStatus()));
            Utils.removeItem(this.session, el);
        });

    }

    @Test
    public void updateItem() {

        this.session.save(testCake);
        Long id = testCake.getId();

        this.testCake.setName("new_test_name_for_update_" + this.hashCode());

        Utils.execute(this.cakeRepository.updateItem(this.testCake));

        Cake updatedCakeInRepository = session.get(Cake.class, id);

        TestCase.assertTrue(this.testCake.getName().equals(updatedCakeInRepository.getName()));
        TestCase.assertTrue(this.testCake.getStatus().equals(updatedCakeInRepository.getStatus()));

        Utils.removeItem(this.session, this.testCake);

    }

    @Test
    public void removeItem() {

        this.session.save(this.cakeToDelete);

        Long id = this.cakeToDelete.getId();

        Utils.execute(this.cakeRepository.removeItem(this.cakeToDelete));

        session.clear();

        Cake cake = session.get(Cake.class, id);

        TestCase.assertNull(cake);
    }

    @Test
    public void getTotal() {

        this.cakesList.forEach(session::save);

        Long total = Utils.getData(this.cakeRepository.getTotal(cakeFilter), 0L);

        System.out.println("Current total : " + this.cakesList.size());
        System.out.println("Repository total : " + total);

        TestCase.assertEquals(total,new Long(this.cakesList.size()));

        this.cakesList.forEach(cake -> Utils.removeItem(this.session,cake));

    }
}