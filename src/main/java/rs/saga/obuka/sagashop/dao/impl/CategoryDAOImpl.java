package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.CategoryDAO;
import rs.saga.obuka.sagashop.domain.Category;

/**
 * @author: Ana Dedović
 * Date: 25.06.2021.
 **/
@Repository
public class CategoryDAOImpl extends AbstractDAOImpl<Category, Long> implements CategoryDAO {
}
