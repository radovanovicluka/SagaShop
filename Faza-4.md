## Faza IV - DAO testovi, Audit, Versioning - 2 nedelje

- Uvesti NonTransactional testove na Dao sloju. To su testovi koji testiraju DAO sloj i za to se koristi HibernateTransactionalUtils klasa data u prilogu
```
package rs.saga.obuka.sagashop.test.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public int countAggregation(String tableName){
        return doInTransaction(entityManager1 -> {
            Query query = entityManager1.createNativeQuery("select count(*) as count from " + tableName, Tuple.class);
            Tuple tuple = (Tuple) query.getSingleResult();
            BigInteger count = tuple.get("count", BigInteger.class);
            return count.intValue();
        });
    }
}
```    
> Klasa služi da se iz NotTransactional konteksta (DAO sloj) manipuliše sa bazom podataka, odnosno da se rade insert, update i delete naredbe.

Primer DAO testa za pretragu produkta
```
public class ProductDAOTest extends AbstractDAOTest {

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private HibernateTransactionUtils hibernateTransactionUtils;

  @Test
  public void filter() {

    //pripremamo proizvod 1 - categoryRacunari
    final Long categoryID = hibernateTransactionUtils.doInTransaction(entityManager -> {
      Product productAuroraRacunar = ProductBuilder.productAuroraRacunar();
      Category categoryRacunari = CategoryBuilder.categoryRacunari();
      productAuroraRacunar.getCategories().add(categoryRacunari);
      categoryRacunari.getProducts().add(productAuroraRacunar);
      entityManager.persist(productAuroraRacunar);
      return productAuroraRacunar.getCategories().iterator().next().getId();
    });

    //pripremamo proizvod 2 - categoryRacunari
    hibernateTransactionUtils.doInTransaction(entityManager -> {
      Product productLenovoRacunar = ProductBuilder.productLenovoRacunar();
      Category categoryRacunari = entityManager.find(Category.class, categoryID);
      productLenovoRacunar.getCategories().add(categoryRacunari);
      categoryRacunari.getProducts().add(productLenovoRacunar);
      entityManager.persist(productLenovoRacunar);
    });

    //pripremamo proizvod 3 - categoryBelaTehnika
    hibernateTransactionUtils.doInTransaction(entityManager -> {
      Category categoryBelaTehnika = CategoryBuilder.categoryBelaTehnika();
      Product productIndesitFrizider = ProductBuilder.productIndesitFrizider();
      productIndesitFrizider.getCategories().add(categoryBelaTehnika);
      categoryBelaTehnika.getProducts().add(productIndesitFrizider);
      entityManager.persist(productIndesitFrizider);
    });

    //pretrazujemo proizvod po nazivu - contains
    ProductCriteria productCriteria = new ProductCriteria();
    productCriteria.setName("GIGATRON AURORA STANDARD");
    List<Product> productList = productDAO.filter(productCriteria);
    assertEquals(1, productList.size());

    //pretrazujemo proizvod po ceni - equals
    productCriteria = new ProductCriteria();
    productCriteria.setPrice(BigDecimal.valueOf(50000.00));
    productList = productDAO.filter(productCriteria);
    assertEquals(1, productList.size());

    //pretrazujemo proizvod po nazivu kategorije
    productCriteria = new ProductCriteria();
    productCriteria.setCategoryName("Računari i komponente");
    productList = productDAO.filter(productCriteria);
    assertEquals(2, productList.size());

    //pretrazujemo proizvod po kolicinii - less than
    productCriteria = new ProductCriteria();
    productCriteria.setQuantity(5);
    productList = productDAO.filter(productCriteria);
    assertEquals(productList.size(), 2);

  }

  @AfterEach
  public void tearDown(){
    hibernateTransactionUtils.clearDatabase();
  }

}
```

- Dodati audit preko SpringData koncepta - DatumKreiranja, DatumPoslednjePromene, Kreirao, Promenio u svim entitetima.
  Pogledati opis: https://dzone.com/articles/spring-data-jpa-auditing-automatically-the-good-stuff
  - Napraviti 2 dml fajla. Smestiti ih u test/resources/db folder jer ce se koristiti u testovima
    - Prvi DML fajl ubacuje default user-a.
    - Drugi DML fajl briše default user-a.
    - Zakucati ID user-a na 1. Datume staviti na sysdate, dok atributima created_by i modified_by dodeliti vrednost 1, dakle sam sebe je dodao.
    - Delete skripta mora da sadrži SET FOREIGN_KEY_CHECKS=0; pre delete-a i SET FOREIGN_KEY_CHECKS=0; posle delete-a.
      ```
      SET FOREIGN_KEY_CHECKS=0;
      delete from user where username = 'default';
      SET FOREIGN_KEY_CHECKS=1;
      ```
  - Napraviti Audit klasu koju skoro svi entiteti nasleđuju. Nije potrebno da pratimo audit za Role i Item entitete.
  - U svim AbstractTest klasama dodati da se učitaju skripe, koji popunjavaju i brisu jednog default korisnika. Prva skripta se poziva pre, a druga posle izvrsavanja testova. Korisnika ubaciti u SpringSecurityContext
    ```
    @Sql(scripts="classpath:db/test-data-before.sql", executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts="classpath:db/test-data-after.sql", executionPhase=Sql.ExecutionPhase.AFTER_TEST_METHOD)
    ```
  - U service testovima (save i update za Category, Product, User i ShoppingCart), u assert delu, dodati i audit proveru (napraviti genericku testAudit metodu u AbstractTest)
    - Proveriti sva 4 audit atributa da li su NotNull, kao i da li su isti User i SecurityContext i User u Audit klasi.
  - Napraviti AuditDTO klasu. Dodati je kao kompozicijdu u Info I Result klase u entitetima Category, Product, User, ShoppingCart.
    > Detalje o konceptu kompozicije pročitati:
    -  Effective Java: Item 16
    -  Teorija: https://javarevisited.blogspot.com/2013/06/why-favor-composition-over-inheritance-java-oops-design.html
    -  Primeri: http://efectivejava.blogspot.com/2013/07/item-16-favor-composition-ovr.html
    > AuditDto klasa treba da ima sve audit atribute, s tim sto su createdBy i modifiedBy tipa String (konkatenacija ima + prezime)
  - Prilagoditi mapere da u @AfterMapping-u popune audit podatke za DTO objekte da bi korisnik mogao da ih vidi.
    Primer:
    ```
      @AfterMapping
      default void afterMappingDTO(Category category, @MappingTarget CategoryInfo categoryInfo){
        AuditMapper.INSTANCE.fillAudit(category, categoryInfo.getAudit());
      }
    ```
    ```
    @Mapper
    public interface AuditMapper {
      AuditMapper INSTANCE = Mappers.getMapper( AuditMapper.class );
  
      default void fillAudit(Audit<Long> audit, @MappingTarget AuditDTO auditDTO){
          if(audit.getCreatedBy() != null){
              auditDTO.setCreatedBy(audit.getCreatedBy().getName() + " " + audit.getCreatedBy().getSurname());
              auditDTO.setLastModifiedBy(audit.getLastModifiedBy().getName() + " " + audit.getLastModifiedBy().getSurname());
              auditDTO.setCreationDate(audit.getCreationDate());
              auditDTO.setLastModifiedDate(audit.getLastModifiedDate());
          }
          auditDTO.setVersion(audit.getVersion());
      }
  
      default void fillAudit(AuditDTO auditDTO, @MappingTarget Audit<Long> audit){
          audit.setVersion(auditDTO.getVersion());
      }
    }
    ```

  - U Rest unit testovima proveriti da li su audit DTO podaci  popunjeni. Dovoljno je proveru raditi samo na getById testovima (ali moze i na drugim)
  - Napisati integracioni rest test za kategoriju, findById, koji proverava da li je audit popunjen.

- Uvesti @Version kolonu koja omogucava da 2 korisnika ne mogu da menjaju objekat u isto vreme
  - Pročitati blogove koji se tiču *optimistic locking-a* i baze. Veoma je bitno razumeti problem i predloženo rešenje
    - https://vladmihalcea.com/a-beginners-guide-to-database-locking-and-the-lost-update-phenomena/
    - https://vladmihalcea.com/how-does-mvcc-multi-version-concurrency-control-work/
    - https://vladmihalcea.com/preventing-lost-updates-in-long-conversations/
    - http://www.byteslounge.com/tutorials/jpa-entity-versioning-version-and-optimistic-locking
    - https://www.thoughts-on-java.org/hibernate-tips-use-timestamp-versioning-optimistic-locking/
  - Napraviti  entity klasu Version i u nju smestiti samo atribut version. On može da bude tipa Integer/Long ili LocaleDateTime. Datum posledenje promene bi bio idealan kandidat, međutim postoji bug koji to ne dozvoljava. Zato je potrebno dodati novi atribut, version, koji je tipa Long.
  - Staviti da Audit klasa nasleđuje Version klasu, da bi svi entiteti koji nasleđuju Audit istovremeno bili i verzionisani. Item i Role nema potrebe da verzionišemo.
  - Nakon ove izmene, skoro svi testovi će da padaju, a razlog je što ili mapper nije kopirao version kolonu, ili zato što menjamo neki objekat a da je neka druga transakcija ga već promenila, pa se dobija StaleDataException. Zato dodati u AuditDto *version* atribut, i svuda gde se popunjava audit, popuniti version takođe.
  - Ispraviti sve testove. Tu imamo 2 problema (exceptiona)
    - 	*org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : rs.saga.obuka.sagashop.entity.ShoppingCart.user -> rs.saga.obuka.sagashop.entity.User; ... *
         Ovo znaci da version kolona nije popunjena na User objektu od ShoppingCart objekta. Dakle sve version kolone moraju da se popune u relaciji, ne smeju da budu null. Savet je da se version atribut inicijalizuje u klasi.
         ```
         public abstract class Version<PK extends Serializable> extends BaseEntity<PK> {

            @Column
            @javax.persistence.Version
            private Integer version = 1;
          
            public Long getVersion() {
            return version;
            }
          
            public void setVersion(Long version) {
              this.version = version;
            }
         }
         ```
    - *org.springframework.orm.ObjectOptimisticLockingFailureException: Object of class [rs.saga.obuka.sagashop.entity.ShoppingCart] with identifier [1]: optimistic locking failed; nested exception is org.hibernate.StaleObjectStateException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect) : [rs.saga.obuka.sagashop.entity.ShoppingCart#1]*  
      Ovo znači da se menja red koji je neko drugi promenio. U testovima je ovo često slučaj kada se pozove save, mi dobijemo DTO nazad, ali u njemu version nije promenjen  (povećan za 1) jer je flush uradjen posle mapiranja iz entiteta u DTO i samim tim imamo zastareli podatak u testu. U tim slučajevima pozvati findOne metodu sa servisa da se osveže podaci, pa posle zvati save/update ili neku drugu metodu.
  - Napisati test koji testira OptimisticLocking. Test napisati za Category,Product i User entitete u servis testovima.
    Primer:
    ```
    @Test
    public void testVersion() throws ServiceException {
        //cuvamo kategoriju, verzija dobija vrednost 1
        CreateCategoryCmd cmd = new CreateCategoryCmd("Tehnika", "Tv, CD, USB");
        CategoryInfo category = categoryService.save(cmd);
        assertEquals(1, category.getAudit().getVersion());

        //menjamo kategoriju, verzija dobija vrednost 2
        UpdateCategoryCmd updateCategoryCmd = new UpdateCategoryCmd(category.getId(),  category.getName(), category.getDescription(), 0L);
        updateCategoryCmd.setName("promenjena kategorija");
        categoryService.update(updateCategoryCmd);
        category = categoryService.findById(category.getId());
        assertEquals(2, category.getAudit().getVersion());

        //menjmo verziju kroz transakciju i vrednost joj je 3. Cuvamo izmenjenu kategoriju da na njoj probamo OptimisticLockingException
        final Long id = category.getId();
        final Category category1 = hibernateTransactionUtils.doInTransaction(entityManager -> {
            Category dbCat = entityManager.find(Category.class, id);
            dbCat.setName("version name 1");
            entityManager.persist(dbCat);
            return dbCat;
        });

        //menjamo kategoriju i verzija dobija vrednost 4
        hibernateTransactionUtils.doInTransaction(entityManager -> {
            Category dbCat = entityManager.find(Category.class, id);
            dbCat.setName("version name 2");
            entityManager.persist(dbCat);
        });
        
        // kategoriju sa vrednoscu verzije 3 pokusavamo da izmenimo i tada se desava exception,
        // odnosno hibernate sprecava da promenimo red koji je druga transakcija promenila
        try {
            hibernateTransactionUtils.doInTransaction(entityManager -> {
                category1.setName("version name 3");
                try {
                    entityManager.merge(category1);
                    fail("mora da pukne zbog verzije");
                } catch (javax.persistence.OptimisticLockException e) {
                    assertTrue(true, "druga transakcija promenila slog");
                }
            });
        }catch (org.springframework.transaction.UnexpectedRollbackException e){
            assertTrue(true, "rollback");
        }

    }
    ```

**Literatura**
- [Audit and Versiongin](./AuditAndVersioning.md) 