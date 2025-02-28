package rs.saga.obuka.sagashop.unit.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractDAOTest;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.User;
import rs.saga.obuka.sagashop.utils.HibernateTransactionUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDAOTest extends AbstractDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HibernateTransactionUtils hibernateTransactionUtils;

    @Test
    void findByUsername() {
        hibernateTransactionUtils.doInTransaction(entityManager -> {
            User user = new User();

            user.setUsername("lukar");
            user.setPassword("pass");
            user.setName("Luka");
            user.setSurname("R");

            entityManager.persist(user);
        });

        hibernateTransactionUtils.doInTransaction(entityManager -> {
            User user = new User();

            user.setUsername("novicat");
            user.setPassword("pass");
            user.setName("Novica");
            user.setSurname("T");

            entityManager.persist(user);
        });

        User user = userDAO.findByUsername("lukar");
        assertEquals("lukar", user.getUsername());
        assertEquals("pass", user.getPassword());
        assertEquals("Luka", user.getName());
        assertEquals("R", user.getSurname());
    }

    @AfterEach
    public void tearDown() {
        hibernateTransactionUtils.clearDatabase();
    }

}
