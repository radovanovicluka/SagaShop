package rs.saga.obuka.sagashop.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ninovic on 6/6/2017.
 */
@SuppressWarnings({"JpaQlInspection", "unused", "Duplicates"})
@Component
public class HibernateTransactionUtils {

    @PersistenceContext
    private EntityManager entityManager;

    protected final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread bob = new Thread(r);
        bob.setName("RestCoreTest");
        return bob;
    });

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private TransactionTemplate transactionTemplate;

    public <T> T doInTransaction(TransactionCallable<T> callable) {
        return transactionTemplate.execute(status -> callable.execute(entityManager));
    }

    public void doInTransaction(TransactionVoidCallable callable) {
        transactionTemplate.execute(status -> {
            callable.execute(entityManager);
            return null;
        });
    }

    @FunctionalInterface
    public interface TransactionCallable<T> extends EntityManagerCallable<T> {
    }

    @FunctionalInterface
    public interface EntityManagerCallable<T> {
        T execute(EntityManager entityManager);
    }

    @FunctionalInterface
    public interface TransactionVoidCallable extends EntityManagerVoidCallable {
    }

    @FunctionalInterface
    public interface EntityManagerVoidCallable {
        void execute(EntityManager entityManager);
    }

    @FunctionalInterface
    public interface SaveEntityVoidCallable<E> {
        E execute();
    }

    @FunctionalInterface
    public interface VoidCallable extends Callable<Void> {

        void execute();

        default Void call() {
            execute();
            return null;
        }
    }

    public void clearDatabase() {
        transactionTemplate.execute((TransactionCallback<Void>) transactionStatus -> {
            entityManager.createNativeQuery("delete from category_product").executeUpdate();
            entityManager.createNativeQuery("delete from user_role").executeUpdate();
            entityManager.createQuery("delete from Item ").executeUpdate();
            entityManager.createQuery("delete from ShoppingCart ").executeUpdate();
            entityManager.createQuery("delete from PayPalAccount ").executeUpdate();
            entityManager.createQuery("delete from Category ").executeUpdate();
            entityManager.createQuery("delete from Product").executeUpdate();
            entityManager.createQuery("delete from User where id > 1").executeUpdate();
            entityManager.createQuery("delete from Role").executeUpdate();
            entityManager.flush();
            return null;
        });
    }

    public void executeSync(VoidCallable callable) {
        executeSync(Collections.singleton(callable));
    }

    public void executeSync(Collection<VoidCallable> callables) {
        try {
            List<Future<Void>> futures = executorService.invokeAll(callables);
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Collection<T> getAll(String entity, Class<T> clazz) {
        return entityManager.createQuery("select e from " + entity + " e", clazz).getResultList();
    }

    public int countAggregation(String tableName) {
        return doInTransaction(entityManager1 -> {
            Query query = entityManager1.createNativeQuery("select count(*) as count from " + tableName, Tuple.class);
            Tuple tuple = (Tuple) query.getSingleResult();
            BigInteger count = tuple.get("count", BigInteger.class);
            return count.intValue();
        });
    }
}