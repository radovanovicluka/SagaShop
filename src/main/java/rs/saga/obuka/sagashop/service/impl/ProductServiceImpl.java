package rs.saga.obuka.sagashop.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rs.saga.obuka.sagashop.dao.CategoryDAO;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.product.*;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.exception.ErrorCode;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.ProductMapper;
import rs.saga.obuka.sagashop.service.ProductService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    @Override
    public Product save(CreateProductCmd cmd) throws ServiceException {
        Product product = ProductMapper.INSTANCE.createProductCmdToProduct(cmd);

        if ( cmd.getCategoryIds() != null ) {
            for (Long id : cmd.getCategoryIds()) {
                Category category = categoryDAO.findOne(id);

                if (category == null) {
                    throw new ServiceException(ErrorCode.ERR_GEN_002, "Category not found! ID = " + id);
                }

                product.getCategories().add(categoryDAO.findOne(id));
            }
        }

        try {
            product = productDAO.save(product);
        }
        catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Saving of product failed!", e);
        }

        return product;
    }

    @Override
    public List<ProductResult> findAll() {
        return ProductMapper.INSTANCE.listProductToListProductResult(productDAO.findAll());
    }

    @Override
    public ProductInfo findById(Long id) {
        return ProductMapper.INSTANCE.productToProductInfo(productDAO.findOne(id));
    }

    @Override
    public void update(UpdateProductCmd cmd) throws ServiceException {

        Product product;

        try {
            product = productDAO.findOne(cmd.getId());

            if ( product == null ) {
                throw new ServiceException(ErrorCode.ERR_GEN_002, "Product not found! ID = " + cmd.getId());
            }

            if ( cmd.getAddCategoryIds() != null ) {
                if (cmd.getAddCategoryIds().stream().anyMatch(cmd.getRemoveCategoryIds()::contains)) {
                    throw new ServiceException(ErrorCode.ERR_GEN_005, "Bad category input data - Duplicates");
                }

                for (Long id : cmd.getAddCategoryIds()) {
                    Category category = categoryDAO.findOne(id);

                    if (category == null) {
                        throw new ServiceException(ErrorCode.ERR_GEN_002, "Category not found! ID = " + id);
                    }

                    product.getCategories().add(category);
                }
            }

            if ( cmd.getRemoveCategoryIds() != null ) {
                for (Long id : cmd.getRemoveCategoryIds()) {
                    Category category = categoryDAO.findOne(id);

                    if (category == null) {
                        throw new ServiceException(ErrorCode.ERR_GEN_002, "Category not found! ID = " + id);
                    }

                    product.getCategories().remove(category);
                }
            }

            ProductMapper.INSTANCE.updateProductCmdToProduct(product, cmd);
            productDAO.merge(product);

        }
        catch ( DAOException e ) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_005, "Update of product failed!",e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        Product product = productDAO.findOne(id);

        if ( product != null ) {
            try {
                productDAO.delete(product);
            }
            catch (DAOException e) {
                LOGGER.error(e.getMessage());
                throw new ServiceException(ErrorCode.ERR_GEN_003, "Deleting of product failed!",e);
            }
        }
        else {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "Product does not exist!");
        }
    }

    @Override
    public List<ProductResult> findByName(String name) {
        return ProductMapper.INSTANCE.listProductToListProductResult(productDAO.findByName(name));
    }

    @Override
    public List<ProductResult> findByPrice(double price) {
        return ProductMapper.INSTANCE.listProductToListProductResult(productDAO.findByPrice(price));
    }

    @Override
    public List<ProductResult> findByCategory(String categoryName) {
        return ProductMapper.INSTANCE.listProductToListProductResult(productDAO.findByCategory(categoryName));
    }
}
