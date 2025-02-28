package rs.saga.obuka.sagashop.dao;

import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.paging.PagingResponse;
import rs.saga.obuka.sagashop.dto.product.ProductCriteria;
import rs.saga.obuka.sagashop.dto.product.ProductProjection;
import rs.saga.obuka.sagashop.dto.product.ProductResult;

import java.util.List;

public interface ProductDAO extends AbstractDAO<Product, Long> {

    PagingResponse<ProductProjection> findProducts(ProductCriteria criteria);

    List<Product> findByName(String name);

    List<Product> findByPrice(double price);

    List<Product> findByCategory(String categoryName);

    List<Product> findByQuantity(int quantity);

    boolean isProductUnique(String productName);

}
