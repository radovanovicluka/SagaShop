package rs.saga.obuka.sagashop.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.CategoryService;

import java.util.List;
import javax.validation.Valid;

/**
 * @author: Ana Dedović
 * Date: 30.06.2021.
 **/
@RestController
@RequestMapping("/category")
public class CategoryRest {

    private final CategoryService categoryService;

    public CategoryRest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category save(@RequestBody @Valid CreateCategoryCmd cmd) throws ServiceException {
        return categoryService.save(cmd);
    }

    @GetMapping
    @ResponseBody
    public List<CategoryResult> findAll() {
        return categoryService.findAll();
    }

    @GetMapping(value = "/{id}")
    public CategoryInfo findById(@PathVariable long id) {
        return categoryService.findById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdateCategoryCmd cmd) throws ServiceException {
        categoryService.update(cmd);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        categoryService.delete(id);
    }

}
