package rs.saga.obuka.sagashop.unit.rest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import rs.saga.obuka.sagashop.AbstractUnitRestTest;
import rs.saga.obuka.sagashop.builder.ProductBuilder;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.audit.AuditDTO;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.product.CreateProductCmd;
import rs.saga.obuka.sagashop.dto.product.ProductInfo;
import rs.saga.obuka.sagashop.dto.product.ProductResult;
import rs.saga.obuka.sagashop.dto.product.UpdateProductCmd;
import rs.saga.obuka.sagashop.rest.ProductRest;
import rs.saga.obuka.sagashop.service.ProductService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("REST")
@WebMvcTest(controllers = ProductRest.class)
public class ProductRestTest extends AbstractUnitRestTest {

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "ADMIN")
    public void saveProduct() throws Exception {
        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(50), "Mis - Logitech", "Laserski mis", 10, null, null);
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
        Product product = ProductBuilder.misProduct();

        doReturn(product).when(productService).save(any(CreateProductCmd.class));

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    public void findAll() throws Exception {
        List<ProductResult> results = new ArrayList<>();
        results.add(new ProductResult(1L, new BigDecimal(30), "Product1", "Desc 1", 5,
                new ArrayList<CategoryResult>(), new AuditDTO()));
        results.add(new ProductResult(2L, new BigDecimal(50), "Product2", "Desc 2", 10,
                new ArrayList<CategoryResult>(), new AuditDTO()));

        doReturn(results).when(productService).findAll();

        String path = "/product";
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].id").value(results.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").isString())
                .andExpect(jsonPath("$.[0].name").value(results.get(0).getName()))
                .andExpect(jsonPath("$.[1]").exists())
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].id").value(results.get(1).getId()))
                .andExpect(jsonPath("$.[1].name").isString())
                .andExpect(jsonPath("$.[1].name").value(results.get(1).getName()));
    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "USER")
    public void findById() throws Exception {
        ProductInfo info = new ProductInfo(1L, new BigDecimal(100), "Product", "Desc", 10,
                new ArrayList<CategoryInfo>(), new AuditDTO(
                "default", Date.valueOf(LocalDate.now()), "default", Date.valueOf(LocalDate.now()), 1L
        ));

        doReturn(info).when(productService).findById(anyLong());

        String path = "/product/1";

        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(info.getId()))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value(info.getName()))
                .andExpect(jsonPath("$.audit.createdBy").exists())
                .andExpect(jsonPath("$.audit.lastModifiedBy").exists())
                .andExpect(jsonPath("$.audit.creationDate").exists())
                .andExpect(jsonPath("$.audit.lastModifiedDate").exists());
    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "ADMIN")
    public void updateCategory() throws Exception {
        UpdateProductCmd cmd = new UpdateProductCmd(1L, new BigDecimal(20), "Updated name", "Updated description", 15, null, null);
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        doNothing().when(productService).update(any(UpdateProductCmd.class));

        mockMvc.perform(put("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", password = "user", authorities = "ADMIN")
    public void deleteCategory() throws Exception {
        doNothing().when(productService).delete(anyLong());

        mockMvc.perform(delete("/product/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
