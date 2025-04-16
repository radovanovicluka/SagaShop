package rs.saga.obuka.sagashop.unit.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractDAOTest;
import rs.saga.obuka.sagashop.builder.CategoryBuilder;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.utils.HibernateTransactionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductDAOTest extends AbstractDAOTest {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private HibernateTransactionUtils hibernateTransactionUtils;

    @Test
    @Disabled
    @Transactional
    void save() throws Exception {
        List<Category> category = new ArrayList<Category>();

        category.add(new Category("Entertainment", "desc", null));

        Product product1 = new Product(new BigDecimal(10), "TV", "HDTV",
                10, category);

        Product product2 = new Product(new BigDecimal(20), "PC", "Gaming",
                6, category);

        product1 = productDAO.save(product1);
        product2 = productDAO.save(product2);

        assertNotNull(product1);
        assertNotNull(product2);
        assertNotNull(product1.getId());
        assertNotNull(product2.getId());
    }

    @Test
    public void filter() {

        //pripremamo proizvod 1 - categoryRacunari
        final Long categoryID = hibernateTransactionUtils.doInTransaction(entityManager -> {
            Product productAuroraRacunar = new Product();

            productAuroraRacunar.setName("GIGATRON AURORA STANDARD");
            productAuroraRacunar.setPrice(new BigDecimal(10000));
            productAuroraRacunar.setQuantity(3);

            Category categoryRacunari = CategoryBuilder.categoryRacunari();
            productAuroraRacunar.getCategories().add(categoryRacunari);
            categoryRacunari.getProducts().add(productAuroraRacunar);
            entityManager.persist(productAuroraRacunar);
            return productAuroraRacunar.getCategories().iterator().next().getId();
        });

        //pripremamo proizvod 2 - categoryRacunari
        hibernateTransactionUtils.doInTransaction(entityManager -> {
            Product productLenovoRacunar = new Product();

            productLenovoRacunar.setName("GIGATRON LENOVO STANDARD");
            productLenovoRacunar.setPrice(new BigDecimal(50000));
            productLenovoRacunar.setQuantity(10);

            Category categoryRacunari = entityManager.find(Category.class, categoryID);
            productLenovoRacunar.getCategories().add(categoryRacunari);
            categoryRacunari.getProducts().add(productLenovoRacunar);
            entityManager.persist(productLenovoRacunar);
        });

        //pripremamo proizvod 3 - categoryBelaTehnika
        hibernateTransactionUtils.doInTransaction(entityManager -> {
            Category categoryBelaTehnika = CategoryBuilder.categoryBelaTehnika();

            Product productIndesitFrizider = new Product();

            productIndesitFrizider.setName("INDESIT FRIZIDER");
            productIndesitFrizider.setPrice(new BigDecimal(80000));
            productIndesitFrizider.setQuantity(4);

            productIndesitFrizider.getCategories().add(categoryBelaTehnika);
            categoryBelaTehnika.getProducts().add(productIndesitFrizider);
            entityManager.persist(productIndesitFrizider);
        });

        List<Product> productList = productDAO.findByName("GIGATRON AURORA STANDARD");
        assertEquals(1, productList.size());

        productList = productDAO.findByPrice(50000.00);
        assertEquals(1, productList.size());

        productList = productDAO.findByCategory("Računari i komponente");
        assertEquals(2, productList.size());

        productList = productDAO.findByQuantity(5);
        assertEquals(2, productList.size());

    }

    @Test
    @Disabled
    @Transactional
    void findByName() throws Exception {
        List<Category> category = new ArrayList<Category>();

        category.add(new Category("Entertainment", "desc", null));

        Product product1 = new Product(new BigDecimal(10), "televizor", "HDTV",
                10, category);

        Product product2 = new Product(new BigDecimal(20), "kompjuter", "Gaming",
                6, category);

        product1 = productDAO.save(product1);
        product2 = productDAO.save(product2);

        List<Product> products1 = productDAO.findByName("ele");
        List<Product> products2 = productDAO.findByName("JuT");
        List<Product> products3 = productDAO.findByName("sn");
        List<Product> products4 = productDAO.findByName("e");

        assertNotNull(products1);
        assertNotNull(products2);
        assertNotNull(products3);
        assertNotNull(products4);
        assertEquals(1, products1.size());
        assertEquals(1, products2.size());
        assertEquals(0, products3.size());
        assertEquals(2, products4.size());
        assertEquals("televizor", products1.get(0).getName());
        assertEquals("kompjuter", products2.get(0).getName());
    }

    @Test
    @Disabled
    @Transactional
    void findByPrice() throws Exception {
        List<Category> category = new ArrayList<Category>();

        category.add(new Category("Entertainment", "desc", null));

        Product product1 = new Product(new BigDecimal(10), "televizor", "HDTV",
                10, category);

        Product product2 = new Product(new BigDecimal(20), "kompjuter", "Gaming",
                6, category);

        product1 = productDAO.save(product1);
        product2 = productDAO.save(product2);

        List<Product> products1 = productDAO.findByPrice(10);
        List<Product> products2 = productDAO.findByPrice(20);
        List<Product> products3 = productDAO.findByPrice(30);

        assertNotNull(products1);
        assertNotNull(products2);
        assertNotNull(products3);
        assertEquals(1, products1.size());
        assertEquals(1, products2.size());
        assertEquals(0, products3.size());
        assertEquals("televizor", products1.get(0).getName());
        assertEquals("kompjuter", products2.get(0).getName());
    }

    @Test
    @Disabled
    @Transactional
    void findByCategory() throws Exception {
        List<Category> category = new ArrayList<Category>();

        category.add(new Category("Entertainment", "desc", null));

        Product product1 = new Product(new BigDecimal(10), "televizor", "HDTV",
                10, category);

        Product product2 = new Product(new BigDecimal(20), "kompjuter", "Gaming",
                6, category);

        product1 = productDAO.save(product1);
        product2 = productDAO.save(product2);

        List<Product> products1 = productDAO.findByCategory("ntEr");
        List<Product> products2 = productDAO.findByCategory("snj");

        assertNotNull(products1);
        assertNotNull(products2);
        assertEquals(2, products1.size());
        assertEquals(0, products2.size());

    }

    @AfterEach
    public void tearDown() {
        hibernateTransactionUtils.clearDatabase();
    }

}
