package rs.saga.obuka.sagashop.integration.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractIntegrationTest;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.product.CreateProductCmd;
import rs.saga.obuka.sagashop.dto.product.ProductInfo;
import rs.saga.obuka.sagashop.dto.product.ProductResult;
import rs.saga.obuka.sagashop.dto.product.UpdateProductCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Service")
public class ProductServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    public void saveProduct() throws ServiceException {
        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(500), "TV - Samsung", "HDTV", 10, null, null);
        Product product = productService.save(cmd);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(cmd.getDescription(), product.getDescription());
        assertEquals(cmd.getName(), product.getName());
    }

    @Test
    public void updateProduct() throws ServiceException {

        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(500), "TV - Samsung", "HDTV", 10, null, null);
        Product product = productService.save(cmd);
        assertNotNull(product);
        assertNotNull(product.getId());

        UpdateProductCmd update = new UpdateProductCmd( product.getId(), product.getPrice(), product.getName(),
                    product.getDescription(), product.getQuantity(), null, null);
        update.setName("update");
        productService.update(update);

        ProductInfo info = productService.findById(product.getId());
        assertNotNull(info);
        assertEquals(product.getId(), info.getId());
        assertEquals("update", info.getName());

    }

    @Test
    public void deleteProduct() throws ServiceException {

        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(500), "TV - Samsung", "HDTV", 10, null, null);
        Product product = productService.save(cmd);
        assertNotNull(product);
        assertNotNull(product.getId());

        productService.delete(product.getId());

        ProductInfo info = productService.findById(product.getId());
        assertNull(info);
    }

    @Test
    public void findOne() throws ServiceException {

        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(500), "TV - Samsung", "HDTV", 10, null, null);
        Product product = productService.save(cmd);
        assertNotNull(product);
        assertNotNull(product.getId());

        ProductInfo info = productService.findById(product.getId());
        assertNotNull(info);
        assertEquals(product.getId(), info.getId());
        assertEquals("TV - Samsung", info.getName());
    }

    @Test
    public void findAll() throws ServiceException {

        CreateProductCmd cmd1 = new CreateProductCmd(new BigDecimal(500), "TV - Samsung", "HDTV", 10, null, null);
        Product product1 = productService.save(cmd1);
        assertNotNull(product1);
        assertNotNull(product1.getId());

        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(600), "Mis - Logitech", "Laserski", 15, null, null);
        Product product2 = productService.save(cmd);
        assertNotNull(product2);
        assertNotNull(product2.getId());

        List<ProductResult> results = productService.findAll();
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getName().equals("TV - Samsung")));
        assertTrue(results.stream().anyMatch(e -> e.getName().equals("Mis - Logitech")));
    }

}
