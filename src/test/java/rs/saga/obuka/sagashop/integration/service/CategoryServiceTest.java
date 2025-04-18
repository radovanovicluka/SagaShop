package rs.saga.obuka.sagashop.integration.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractIntegrationTest;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: Ana Dedović
 * Date: 13.07.2021.
 */
@Tag("Service")
public class CategoryServiceTest extends AbstractIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void saveCategory() throws ServiceException {
        CreateCategoryCmd cmd = new CreateCategoryCmd("Tehnika", "Tv, CD, USB", null, null);
        Category category = categoryService.save(cmd);
        assertNotNull(category.getId());
        assertEquals(cmd.getDescription(), category.getDescription());
        assertEquals(cmd.getCategoryName(), category.getCategoryName());
        assertNotNull(category.getCreationDate());
        assertNotNull(category.getCreatedBy());
        assertNotNull(category.getLastModifiedBy());
        assertNotNull(category.getLastModifiedDate());
    }

    @Test
    public void updateCategory() throws ServiceException {
        //cuvamo kategoriju
        CreateCategoryCmd cmd = new CreateCategoryCmd("Tehnika", "Tv, CD, USB", null, null);
        Category category = categoryService.save(cmd);
        assertNotNull(category.getId());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //menjamo kategoriju
        UpdateCategoryCmd updateCategoryCmd = new UpdateCategoryCmd(category.getId(), category.getCategoryName(), category.getDescription(), null, null);
        updateCategoryCmd.setCategoryName("promenjena kategorija");
        categoryService.update(updateCategoryCmd);

        //proveravamo da li je kategorija promenjena
        CategoryInfo categoryInfo = categoryService.findById(category.getId());
        assertEquals("promenjena kategorija", categoryInfo.getCategoryName());
        assertNotNull(categoryInfo.getAudit().getCreationDate());
        assertNotNull(categoryInfo.getAudit().getCreatedBy());
        assertNotNull(categoryInfo.getAudit().getLastModifiedBy());
        assertNotNull(categoryInfo.getAudit().getLastModifiedDate());
        assertNotEquals(categoryInfo.getAudit().getCreationDate(), categoryInfo.getAudit().getLastModifiedDate());
    }

    @Test
    public void deleteCategory() throws ServiceException {
        //kreiramo kategoriju
        CreateCategoryCmd cmd = new CreateCategoryCmd("Tehnika", "Tv, CD, USB", null, null);
        Category category = categoryService.save(cmd);
        assertNotNull(category.getId());

        //brisemo kategoriju
        categoryService.delete(category.getId());

        //proveravamo kategoriju
        CategoryInfo categoryInfo = categoryService.findById(category.getId());
        assertNull(categoryInfo);
    }

    @Test
    public void findOne() throws ServiceException {
        //kreiramo kategoriju
        CreateCategoryCmd cmd = new CreateCategoryCmd("Tehnika", "Tv, CD, USB", null, null);
        Category category = categoryService.save(cmd);
        assertNotNull(category.getId());

        //proveravamo kategoriju
        CategoryInfo categoryInfo = categoryService.findById(category.getId());
        assertEquals("Tehnika", categoryInfo.getCategoryName());
        assertEquals("Tv, CD, USB", categoryInfo.getDescription());
    }

    @Test
    public void findAll() throws ServiceException {
        //cuvamo kategoriju 1
        CreateCategoryCmd cmd1 = new CreateCategoryCmd("Tehnika", "Tv, CD, USB", null, null);
        categoryService.save(cmd1);

        //cuvamo kategoriju 2
        CreateCategoryCmd cmd2 = new CreateCategoryCmd("Hrana", "Smoki, Cips, Grisine", null, null);
        categoryService.save(cmd2);

        //proveravamo listu kategorija
        List<CategoryResult> categoryResult = categoryService.findAll();
        assertEquals(2, categoryResult.size());
        assertTrue(categoryResult.stream().anyMatch(e -> e.getCategoryName().equals("Tehnika")));
        assertTrue(categoryResult.stream().anyMatch(e -> e.getCategoryName().equals("Hrana")));
    }

}
