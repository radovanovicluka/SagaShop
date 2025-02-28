package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.paging.PagingResponse;
import rs.saga.obuka.sagashop.dto.product.ProductCriteria;
import rs.saga.obuka.sagashop.dto.paging.PagingRequest;
import rs.saga.obuka.sagashop.dto.product.ProductProjection;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDAOImpl extends AbstractDAOImpl<Product, Long> implements ProductDAO {

    public PagingResponse<ProductProjection> findProducts(ProductCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Product> productRoot = query.from(Product.class);

        Join<Product, Category> categoryJoin = productRoot.join("categories", JoinType.LEFT);

        query.select(cb.tuple(
                productRoot.get("id"),
                productRoot.get("name"),
                productRoot.get("price"),
                productRoot.get("quantity"),
                categoryJoin.get("id").alias("categoryId"),
                categoryJoin.get("categoryName")
        ));

        long count = getCount(criteria, cb);

        applyFilters(query, cb, productRoot, criteria);

        query.orderBy(cb.asc(productRoot.get("name")));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        setPagingParameters(typedQuery, criteria.getPagingRequest());

        List<Tuple> tuples = typedQuery.getResultList();
        List<ProductProjection> result = new ArrayList<>();

        for (Tuple tuple : tuples) {
            ProductProjection dto = new ProductProjection();
            dto.setId(tuple.get("id", Long.class));
            dto.setName(tuple.get("name", String.class));
            dto.setPrice(tuple.get("price", BigDecimal.class));
            dto.setQuantity(tuple.get("quantity", Integer.class));
            dto.setCategoryId(tuple.get("categoryId", Long.class));
            dto.setCategoryName(tuple.get("categoryName", String.class));
            result.add(dto);
        }

        return new PagingResponse<>(count, result);
    }

    private long getCount(ProductCriteria criteria, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);
        Join<Product, Category> categoryJoin = root.join("categories", JoinType.LEFT);
        countQuery.select(cb.count(root));
        applyFilters(countQuery, cb, root, criteria);

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private void applyFilters(CriteriaQuery<?> query, CriteriaBuilder cb, Root<Product> root, ProductCriteria criteria) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getName() != null) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (criteria.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }

        if (criteria.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }

        if (criteria.getDescription() != null) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + criteria.getDescription().toLowerCase() + "%"));
        }

        if (criteria.getMinQuantity() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("quantity"), criteria.getMinQuantity()));
        }

        if (criteria.getMaxQuantity() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("quantity"), criteria.getMaxQuantity()));
        }

        if (criteria.getCategoryName() != null) {
            Join<Product, Category> categoryJoin = root.join("categories", JoinType.LEFT);
            predicates.add(cb.equal(categoryJoin.get("categoryName"), criteria.getCategoryName()));
        }

        query.where(predicates.toArray(new Predicate[0]));
    }

    private void setPagingParameters(TypedQuery<?> query, PagingRequest pagingRequest) {
        if (pagingRequest != null) {
            int firstResult = (pagingRequest.getCurrentPage() - 1) * pagingRequest.getRowsPerPage();
            query.setFirstResult(firstResult);
            query.setMaxResults(pagingRequest.getRowsPerPage());
        }
    }

//    public ProductInfo findById(Long id) {
//
//        TypedQuery<ProductInfo> query = entityManager.createQuery(
//                "select new rs.saga.obuka.sagashop.dto.product.ProductInfo(p.id, p.name, p.price, p.quantity) " +
//                        "from Product p where p.id = :id"
//                , ProductInfo.class
//        );
//
//        return query.getSingleResult();
//    }

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

    @Override
    public List<Product> findByQuantity(int quantity) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product).where(cb.lessThan(product.get("quantity"), quantity));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public boolean isProductUnique(String productName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product).where(cb.equal((product.get("name")), productName));

        return entityManager.createQuery(query).getResultList().isEmpty();
    }

}
