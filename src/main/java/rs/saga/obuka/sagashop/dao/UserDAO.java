package rs.saga.obuka.sagashop.dao;

import rs.saga.obuka.sagashop.domain.User;

public interface UserDAO extends AbstractDAO<User, Long> {

    User findByUsername(String username);

    boolean isUserUnique(String username);

}
