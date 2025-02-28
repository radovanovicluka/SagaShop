package rs.saga.obuka.sagashop.unit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import rs.saga.obuka.sagashop.AbstractUnitRestTest;
import rs.saga.obuka.sagashop.builder.CategoryBuilder;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;
import rs.saga.obuka.sagashop.rest.CategoryRest;
import rs.saga.obuka.sagashop.service.CategoryService;

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

/**
 * @author: Ana Dedović
 * Date: 30.06.2021.
 */
@SuppressWarnings({"WeakerAccess"})
@Tag("REST")
@WebMvcTest(controllers = CategoryRest.class)
public class CategoryRestTest extends AbstractUnitRestTest {

    @MockBean
    private CategoryService categoryService;

    @Test
    public void saveCategory() throws Exception {
        CreateCategoryCmd cmd = new CreateCategoryCmd("Tehnika", "TV, USB");
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
        Category category = CategoryBuilder.categoryBelaTehnika();

        doReturn(category).when(categoryService).save(any(CreateCategoryCmd.class));

        mockMvc.perform(post("/category/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    public void findAll() throws Exception {
        List<CategoryResult> results = new ArrayList<>();
        results.add(new CategoryResult(1L, "Category1", "Desc 1"));
        results.add(new CategoryResult(2L, "Category2", "Desc 2"));

        doReturn(results).when(categoryService).findAll();

        String path = "/category/all";
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
        CategoryInfo info = new CategoryInfo(1L, "Category", "Desc");

        doReturn(info).when(categoryService).findById(anyLong());

        String path = "/category/id/1";

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
        UpdateCategoryCmd cmd = new UpdateCategoryCmd(1L, "Updated name", "Updated description");
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);

        doNothing().when(categoryService).update(any(UpdateCategoryCmd.class));

        mockMvc.perform(put("/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteCategory() throws Exception {
        doNothing().when(categoryService).delete(anyLong());

        mockMvc.perform(delete("/category/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
