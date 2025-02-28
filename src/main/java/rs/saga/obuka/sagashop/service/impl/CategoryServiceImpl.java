package rs.saga.obuka.sagashop.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.saga.obuka.sagashop.dao.CategoryDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.exception.ErrorCode;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.CategoryMapper;
import rs.saga.obuka.sagashop.service.CategoryService;

/**
 * @author: Ana Dedović
 * Date: 28.06.2021.
 **/
@SuppressWarnings("Duplicates")
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryDAO categoryDAO;

    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Category save(CreateCategoryCmd cmd) throws ServiceException {
        Category category = CategoryMapper.INSTANCE.createCategoryCmdToCategory(cmd);
        try {
            category = categoryDAO.save(category);
        } catch (DAOException e) {
            LOGGER.error(null, e);
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Saving of category failed!", e);
        }
        return category;
    }

    @Override
    public List<CategoryResult> findAll() {
        return CategoryMapper.INSTANCE.listCategoryToListCategoryResult(categoryDAO.findAll());
    }

    @Override
    public CategoryInfo findById(Long id) {
        return CategoryMapper.INSTANCE.categoryToCategoryInfo(categoryDAO.findOne(id));
    }

    @Override
    public void update(UpdateCategoryCmd cmd) throws ServiceException {
        Category category;
        try {
            // check if entity still exists
            category = categoryDAO.findOne(cmd.getId());
            if (category == null) {
                throw new ServiceException(ErrorCode.ERR_GEN_002);
            }

            CategoryMapper.INSTANCE.updateCategoryCmdToCategory(category, cmd);
            categoryDAO.merge(category);
        } catch (DAOException e) {
            LOGGER.error(null, e);
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Update of category failed!", e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        Category category = categoryDAO.findOne(id);
        if (category != null) {
            try {
                categoryDAO.delete(category);
            } catch (DAOException e) {
                LOGGER.error(null, e);
                throw new ServiceException(ErrorCode.ERR_GEN_001, "Delete of category failed!", e);
            }
        } else {
            throw new ServiceException(ErrorCode.ERR_CAT_001, "Category does not exist!");
        }
    }

}
