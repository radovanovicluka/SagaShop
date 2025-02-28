package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.CategoryDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author: Ana Dedović
 * Date: 25.06.2021.
 **/
@Repository
public class CategoryDAOImpl extends AbstractDAOImpl<Category, Long> implements CategoryDAO {

    @Override
    public boolean isCategoryUnique(String name) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> category = query.from(Category.class);
        query.select(category).where(cb.equal((category.get("categoryName")), name));

        return entityManager.createQuery(query).getResultList().isEmpty();
    }
}
