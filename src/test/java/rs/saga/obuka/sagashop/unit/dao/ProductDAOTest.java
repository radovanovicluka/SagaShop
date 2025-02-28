package rs.saga.obuka.sagashop.unit.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractDAOTest;
import rs.saga.obuka.sagashop.builder.CategoryBuilder;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.paging.PagingRequest;
import rs.saga.obuka.sagashop.dto.paging.PagingResponse;
import rs.saga.obuka.sagashop.dto.product.ProductCriteria;
import rs.saga.obuka.sagashop.dto.product.ProductProjection;
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
    @Transactional
    void findProductsTest() throws Exception {
        List<Category> category1 = new ArrayList<>();
        List<Category> category2 = new ArrayList<>();

        category1.add(new Category("Entertainment", "desc", null));
        category2.add(new Category("IT", "desc", null));
        category2.add(new Category("Gaming", "desc", null));


        Product product1 = new Product(new BigDecimal(10), "televizor", "HDTV",
                10, category1);

        Product product2 = new Product(new BigDecimal(20), "kompjuter", "Gaming",
                6, category1);

        Product product3 = new Product(new BigDecimal(30), "dzojstik", "ojoj",
                8, category2);

        Product product4 = new Product(new BigDecimal(10), "monitor", "HDTV",
                10, category1);

        Product product5 = new Product(new BigDecimal(20), "laptop", "Gaming",
                6, category1);

        Product product6 = new Product(new BigDecimal(30), "tastatura", "ojoj",
                8, category2);

        Product product7 = new Product(new BigDecimal(10), "zvucnik", "HDTV",
                10, category1);

        Product product8 = new Product(new BigDecimal(20), "mis", "Gaming",
                6, category1);

        Product product9 = new Product(new BigDecimal(30), "ekran", "ojoj",
                8, category2);

        product1 = productDAO.save(product1);
        product2 = productDAO.save(product2);
        product3 = productDAO.save(product3);
        product4 = productDAO.save(product4);
        product5 = productDAO.save(product5);
        product6 = productDAO.save(product6);
        product7 = productDAO.save(product7);
        product8 = productDAO.save(product8);
        product9 = productDAO.save(product9);

        ProductCriteria criteria = new ProductCriteria();
        criteria.setPagingRequest(new PagingRequest(0, 4));

        PagingResponse<ProductProjection> result = productDAO.findProducts(criteria);

        assertNotNull(result);
        assertEquals(4, result.getCount());
        assertEquals("televizor", result.getEntityList().get(0).getName());
    }

    @Test
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
