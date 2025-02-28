package rs.saga.obuka.sagashop.dao;

import rs.saga.obuka.sagashop.domain.Product;

import java.util.List;

public interface ProductDAO extends AbstractDAO<Product, Long> {

    public List<Product> findByName(String name);

    public List<Product> findByPrice(double price);

    public List<Product> findByCategory(String categoryName);

    public List<Product> findByQuantity(int quantity);

}
