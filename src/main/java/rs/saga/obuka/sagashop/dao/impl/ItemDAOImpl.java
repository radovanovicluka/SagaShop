package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.ItemDAO;
import rs.saga.obuka.sagashop.domain.Item;

@Repository
public class ItemDAOImpl extends AbstractDAOImpl<Item, Long> implements ItemDAO {
}
