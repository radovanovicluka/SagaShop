package rs.saga.obuka.sagashop.unit.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractDAOTest;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
public class ProductDAOTest extends AbstractDAOTest {

    @Autowired
    private ProductDAO productDAO;

    @Test
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

}
