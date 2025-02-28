package rs.saga.obuka.sagashop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.product.CreateProductCmd;
import rs.saga.obuka.sagashop.dto.product.ProductInfo;
import rs.saga.obuka.sagashop.dto.product.ProductResult;
import rs.saga.obuka.sagashop.dto.product.UpdateProductCmd;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.exception.ErrorCode;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.ProductMapper;
import rs.saga.obuka.sagashop.mapper.UserMapper;
import rs.saga.obuka.sagashop.service.ProductService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product save(CreateProductCmd cmd) throws ServiceException {

        Product product = ProductMapper.INSTANCE.createProductCmdToProduct(cmd);

        try {
            product = productDAO.save(product);
        }
        catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Saving of pproduct failed!", e);
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
                throw new ServiceException(ErrorCode.ERR_GEN_002);
            }

            ProductMapper.INSTANCE.updateProductCmdToProduct(product, cmd);
            productDAO.merge(product);
        }
        catch ( DAOException e ) {
            LOGGER.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Update of product failed!",e);
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
                throw new ServiceException(ErrorCode.ERR_GEN_001, "Deleting of product failed!",e);
            }
        }
        else {
            throw new ServiceException(ErrorCode.ERR_GEN_001, "Producct does not exist!");
        }
    }
}
