package rs.saga.obuka.sagashop.dao;

import rs.saga.obuka.sagashop.exception.DAOException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author: Ana Dedović
 * Date: 25.06.2021.
 **/
@SuppressWarnings("unused")
public interface AbstractDAO<T, PK extends Serializable> {

    T findOne(PK id);

    List<T> findAll();

    T save(T entity) throws DAOException;

    T merge(T entity) throws DAOException;

    void delete(T entity) throws DAOException;

    void deleteById(PK entityId) throws DAOException;

    void deleteAll(Collection<T> collection) throws DAOException;

    void flush();

    void deleteAll() throws DAOException;

}
