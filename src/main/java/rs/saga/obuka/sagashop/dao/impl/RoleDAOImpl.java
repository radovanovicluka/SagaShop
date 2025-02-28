package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.RoleDAO;
import rs.saga.obuka.sagashop.domain.Role;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class RoleDAOImpl extends AbstractDAOImpl<Role, Long> implements RoleDAO {

    @Override
    public Optional<Role> findByName(String name) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> r = cq.from(Role.class);
        cq.select(r).where(cb.equal(r.get("name"), name));

        try {
            return Optional.of(entityManager.createQuery(cq).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }
}
