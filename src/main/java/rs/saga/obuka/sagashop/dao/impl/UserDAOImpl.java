package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.stereotype.Repository;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.User;

@Repository
public class UserDAOImpl extends AbstractDAOImpl<User, Long> implements UserDAO {
}
