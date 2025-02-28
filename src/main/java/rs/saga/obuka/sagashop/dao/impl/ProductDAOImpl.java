package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Product;

@Repository
public class ProductDAOImpl extends AbstractDAOImpl<Product, Long> implements ProductDAO {
}
