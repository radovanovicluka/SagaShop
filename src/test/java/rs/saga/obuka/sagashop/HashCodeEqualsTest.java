package rs.saga.obuka.sagashop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rs.saga.obuka.sagashop.builder.*;
import rs.saga.obuka.sagashop.domain.*;
import rs.saga.obuka.sagashop.utils.HibernateTransactionUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class HashCodeEqualsTest extends AbstractIntegrationTest {

    @Autowired
    private HibernateTransactionUtils hibernateTransactionUtils;

    @Test
    public void testCategory() {

        Category category = CategoryBuilder.categoryBelaTehnika();
        Category category2 = CategoryBuilder.categoryBelaTehnika();
        assertEqualityConstraints(Category.class, category, category2);
    }

    @Test
    @Disabled
    public void testItem() {
        Item item = ItemBuilder.itemPetTelefona();
        Item item2 = ItemBuilder.itemPetTelefona();
        assertEqualityConstraints(Item.class, item, item2);
    }

    @Test
    @Disabled
    public void testPayPalAccount() {
        PayPalAccount payPalAccount = PayPalAccountBuilder.account1Build();
        PayPalAccount payPalAccount2 = PayPalAccountBuilder.account1Build();
        assertEqualityConstraints(PayPalAccount.class, payPalAccount, payPalAccount2);
    }

    @Test
    public void testProduct() {
        Product product = ProductBuilder.sveskaProduct();
        Product product2 = ProductBuilder.sveskaProduct();
        assertEqualityConstraints(Product.class, product, product2);
    }

    @Disabled
    @Test
    public void testRole() {
        Role role = RoleBuilder.roleAdmin();
        Role role2 = RoleBuilder.roleAdmin();
        assertEqualityConstraints(Role.class, role, role2);
    }

    @Test
    public void testShoppingCart() {
        ShoppingCart cart = ShoppingCartBuilder.buildShoppingCart();
        ShoppingCart cart2 = ShoppingCartBuilder.buildShoppingCart();
        assertEqualityConstraints(ShoppingCart.class, cart, cart2);
    }

    @Test
    public void testUser() {
        User user = UserBuilder.userAdmin();
        User user2 = UserBuilder.userAdmin();
        assertEqualityConstraints(User.class, user, user2);
    }

    /**
     *
     * @param clazz class
     * @param entity entity
     * @param <T> entity
     */
    private <T extends BaseEntity<? extends Serializable>> void assertEqualityConstraints(Class<T> clazz, T entity, T entityCopied){

        assertEquals(entity, entityCopied);

        Set<T> tuples = new HashSet<>();

        assertFalse(tuples.contains(entity));
        tuples.add(entity);
        assertTrue(tuples.contains(entity));

        System.out.println("PRE PERSIST");

        hibernateTransactionUtils.doInTransaction(entityManager -> {
            entityManager.persist(entity);
            entityManager.flush();
            assertTrue(tuples.contains(entity), "The entity is found after it's persisted");
        });

        //The entity is found after the entity is detached
        assertTrue(tuples.contains(entity));

        System.out.println("PRE FIND");

        hibernateTransactionUtils.doInTransaction(entityManager -> {
            T _entity = entityManager.find(clazz, entity.getId());
            assertTrue(tuples.contains(_entity), "The entity is found after it's loaded in an other Persistence Context");
            return entity;
        });

        hibernateTransactionUtils.executeSync(() -> hibernateTransactionUtils.doInTransaction(entityManager -> {
            T _entity = entityManager.find(clazz, entity.getId());
            assertTrue(tuples.contains(_entity), "The entity is found after it's loaded in an other Persistence Context and in an other thread");
        }));

        System.out.println("PRE MERGE");

        hibernateTransactionUtils.doInTransaction(entityManager -> {
            T _entity = entityManager.merge(entity);
            assertTrue(tuples.contains(_entity), "The entity is found after it's merged");
        });

    }

    @AfterEach
    public void clear(){
        hibernateTransactionUtils.clearDatabase();
    }
}
