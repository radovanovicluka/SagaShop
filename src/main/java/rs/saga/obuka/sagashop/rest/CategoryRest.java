package rs.saga.obuka.sagashop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: Ana Dedović
 * Date: 30.06.2021.
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryRest {

    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Category save(@RequestBody @Valid CreateCategoryCmd cmd) throws ServiceException {
        return categoryService.save(cmd);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResult> findAll() {
        return categoryService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/{id}")
    public CategoryInfo findById(@PathVariable long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UpdateCategoryCmd cmd) throws ServiceException {
        categoryService.update(cmd);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws ServiceException {
        categoryService.delete(id);
    }

}
