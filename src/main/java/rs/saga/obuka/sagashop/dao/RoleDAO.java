package rs.saga.obuka.sagashop.dao;

import rs.saga.obuka.sagashop.domain.Role;

import java.util.Optional;

public interface RoleDAO extends AbstractDAO<Role, Long> {

    Optional<Role> findByName(String name);

}
