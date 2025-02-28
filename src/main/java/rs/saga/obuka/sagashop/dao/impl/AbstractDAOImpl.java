package rs.saga.obuka.sagashop.dao.impl;

import org.springframework.transaction.annotation.Transactional;
import rs.saga.obuka.sagashop.dao.AbstractDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import rs.saga.obuka.sagashop.exception.DAOException;

/**
 * @author: Ana Dedović
 * Date: 25.06.2021.
 **/
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractDAOImpl<T, PK extends Serializable> implements AbstractDAO<T, PK> {

    protected Class<T> clazz;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractDAOImpl() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected Class<T> entityClass;

    public void setClazz(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    @Override
    public T findOne(PK id) {
        return entityManager.find(clazz, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    @Override
    public T save(T entity) throws DAOException {
        try {
            T persistentEntity;

            entityManager.persist(entity);
            persistentEntity = entity;
            return persistentEntity;
        }catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public T merge(T entity) throws DAOException {
        try{
            return entityManager.merge(entity);
        }catch (Exception e){
            throw new DAOException(e);
        }

    }

    @Override
    public void delete(T entity) throws DAOException {
        try{
            entityManager.remove(entity);
        }catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public void deleteById(PK entityId) throws DAOException {
        try{
            T entity = findOne(entityId);
            if (entityId != null) {
                entityManager.remove(entity);
            }
        }catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public void deleteAll(Collection<T> collection) throws DAOException {
        for (T t : collection) {
            delete(t);
        }
    }

    public void flush() {
        entityManager.flush();
    }

    @Transactional
    public void deleteAll() throws DAOException {
        deleteAll(findAll());
    }


}
