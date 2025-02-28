package rs.saga.obuka.sagashop.unit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import rs.saga.obuka.sagashop.AbstractUnitRestTest;
import rs.saga.obuka.sagashop.builder.ProductBuilder;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;
import rs.saga.obuka.sagashop.dto.product.CreateProductCmd;
import rs.saga.obuka.sagashop.dto.product.ProductInfo;
import rs.saga.obuka.sagashop.dto.product.ProductResult;
import rs.saga.obuka.sagashop.dto.product.UpdateProductCmd;
import rs.saga.obuka.sagashop.rest.ProductRest;
import rs.saga.obuka.sagashop.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("REST")
@WebMvcTest(controllers = ProductRest.class)
public class ProductRestTest extends AbstractUnitRestTest {

    @MockBean
    private ProductService productService;

    @Test
    public void saveProduct() throws Exception {
        CreateProductCmd cmd = new CreateProductCmd(new BigDecimal(50), "Mis - Logitech", "Laserski mis", 10);
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
        Product product = ProductBuilder.misProduct();

        doReturn(product).when(productService).save(any(CreateProductCmd.class));

        mockMvc.perform(post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    public void findAll() throws Exception {
        List<ProductResult> results = new ArrayList<>();
        results.add(new ProductResult(1L, new BigDecimal(30) ,"Product1", "Desc 1", 5, new ArrayList<CategoryResult>()));
        results.add(new ProductResult(2L, new BigDecimal(50), "Product2", "Desc 2", 10, new ArrayList<CategoryResult>()));

        doReturn(results).when(productService).findAll();

        String path = "/product/all";
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
    public void findById() throws Exception {
        ProductInfo info = new ProductInfo(1L, new BigDecimal(100), "Product", "Desc", 10, new ArrayList<CategoryInfo>());

        doReturn(info).when(productService).findById(anyLong());

        String path = "/product/id/1";

        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(info.getId()))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value(info.getName()));
    }

    @Test
    public void updateCategory() throws Exception {
        UpdateProductCmd cmd = new UpdateProductCmd(1L, new BigDecimal(20), "Updated name", "Updated description", 15, new ArrayList<UpdateCategoryCmd>());
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        doNothing().when(productService).update(any(UpdateProductCmd.class));

        mockMvc.perform(put("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInString))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCategory() throws Exception {
        doNothing().when(productService).delete(anyLong());

        mockMvc.perform(delete("/product/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
