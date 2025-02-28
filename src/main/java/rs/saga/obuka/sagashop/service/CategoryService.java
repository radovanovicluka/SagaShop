package rs.saga.obuka.sagashop.service;

import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.*;
import rs.saga.obuka.sagashop.exception.ServiceException;

import java.util.List;

/**
 * @author: Ana Dedović
 * Date: 25.06.2021.
 **/
public interface CategoryService {

    Category save(CreateCategoryCmd cmd) throws ServiceException;

    List<CategoryResult> findAll();

    CategoryInfo findById(Long id);

    void update(UpdateCategoryCmd CategoryDTO) throws ServiceException;

    void delete(Long id) throws ServiceException;

}
