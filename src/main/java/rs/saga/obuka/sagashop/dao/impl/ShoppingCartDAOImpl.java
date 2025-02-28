package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.ShoppingCartDAO;
import rs.saga.obuka.sagashop.domain.ShoppingCart;

@Repository
public class ShoppingCartDAOImpl extends AbstractDAOImpl<ShoppingCart, Long> implements ShoppingCartDAO {
}
