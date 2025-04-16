package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductDAOImpl extends AbstractDAOImpl<Product, Long> implements ProductDAO {

    public List<Product> findByName(String name) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        name = ("%" + name + "%").toLowerCase();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product).where(cb.like(cb.lower(product.get("name")), name)).orderBy(cb.asc(product.get("name")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Product> findByPrice(double price) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product).where(cb.equal(product.get("price"), price));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Product> findByCategory(String categoryName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        String name = ("%" + categoryName + "%").toLowerCase();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        Join<Product, Category> join = product.join("categories");
        query.select(product).distinct(true).where(cb.like(cb.lower(join.get("categoryName")), name))
                .orderBy(cb.asc(product.get("name")));

        return entityManager.createQuery(query).getResultList();
    }

}
