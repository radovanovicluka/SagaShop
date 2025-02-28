package rs.saga.obuka.sagashop.unit.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractDAOTest;
import rs.saga.obuka.sagashop.dao.RoleDAO;
import rs.saga.obuka.sagashop.domain.Role;
import rs.saga.obuka.sagashop.utils.HibernateTransactionUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleDAOTest extends AbstractDAOTest {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private HibernateTransactionUtils hibernateTransactionUtils;

    @Test
    void findByUsername() {
        hibernateTransactionUtils.doInTransaction(entityManager -> {
            Role role = new Role();

            role.setName("ROLE_USER");

            entityManager.persist(role);
        });

        hibernateTransactionUtils.doInTransaction(entityManager -> {
            Role role = new Role();

            role.setName("ROLE_ADMIN");

            entityManager.persist(role);
        });

        Optional<Role> role = roleDAO.findByName("ROLE_ADMIN");
        assertTrue(role.isPresent());
        assertEquals("ROLE_ADMIN", role.get().getName());
    }

    @AfterEach
    public void tearDown() {
        hibernateTransactionUtils.clearDatabase();
    }

}
