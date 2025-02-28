package rs.saga.obuka.sagashop.integration.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import rs.saga.obuka.sagashop.AbstractIntegrationTest;
import rs.saga.obuka.sagashop.domain.*;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryFromProductCmd;
import rs.saga.obuka.sagashop.dto.item.CreateItemCmd;
import rs.saga.obuka.sagashop.dto.item.ItemInfo;
import rs.saga.obuka.sagashop.dto.paypalaccount.CreatePayPalAccountCmd;
import rs.saga.obuka.sagashop.dto.paypalaccount.PayPalAccountInfo;
import rs.saga.obuka.sagashop.dto.product.CreateProductCmd;
import rs.saga.obuka.sagashop.dto.product.ProductInfo;
import rs.saga.obuka.sagashop.dto.shoppingcart.CreateShoppingCartCmd;
import rs.saga.obuka.sagashop.dto.user.CreateUserCmd;
import rs.saga.obuka.sagashop.exception.BudgetExceededException;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.PayPalAccountService;
import rs.saga.obuka.sagashop.service.ProductService;
import rs.saga.obuka.sagashop.service.ShoppingCartService;
import rs.saga.obuka.sagashop.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Service")
public class ShoppingCartServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private PayPalAccountService payPalAccountService;
    @Autowired
    private ProductService productService;

    @Test
    void saveShoppingCart() throws ServiceException {

        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        assertNotNull(user);
        assertNotNull(user.getId());

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        assertNotNull(cart);
        assertNotNull(cart.getId());
        assertEquals("cart", cart.getName());
        assertEquals(user.getId(), cart.getUser().getId());
        assertEquals(Status.NEW, cart.getStatus());
    }

    @Test
    void addItemValid() throws ServiceException {
        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        List<CreateCategoryFromProductCmd> category = new ArrayList<CreateCategoryFromProductCmd>();

        category.add(new CreateCategoryFromProductCmd("Entertainment", "desc"));

        CreateProductCmd productCmd = new CreateProductCmd(new BigDecimal(10), "TV", "HDTV",
                10, category, null);

        Product product = productService.save(productCmd);

        CreateItemCmd cmd = new CreateItemCmd(cart.getId(), product.getId() ,5);

        ItemInfo info = shoppingCartService.addItem(cmd);

        Item item = shoppingCartService.findItemById(info.getId());

        assertNotNull(item);
        assertNotNull(item.getId());
        assertEquals(5, item.getQuantity());
        assertEquals(product.getId(), item.getProduct().getId());
        assertEquals("TV", item.getProduct().getName());
        assertEquals(Status.ACTIVE, item.getShoppingCart().getStatus());
        assertEquals(cart.getId(), item.getShoppingCart().getId());
    }

    @Test
    void removeItem() throws ServiceException {
        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        List<CreateCategoryFromProductCmd> category = new ArrayList<CreateCategoryFromProductCmd>();

        category.add(new CreateCategoryFromProductCmd("Entertainment", "desc"));

        CreateProductCmd productCmd1 = new CreateProductCmd(new BigDecimal(10), "TV", "HDTV",
                10, category, null);

        CreateProductCmd productCmd2 = new CreateProductCmd(new BigDecimal(20), "PC", "Gaming",
                        6, category, null);

        Product product1 = productService.save(productCmd1);
        Product product2 = productService.save(productCmd2);

        CreateItemCmd cmd1 = new CreateItemCmd(cart.getId(), product1.getId() ,5);
        CreateItemCmd cmd2 = new CreateItemCmd(cart.getId(), product2.getId() ,5);

        ItemInfo info1 = shoppingCartService.addItem(cmd1);
        ItemInfo info2 = shoppingCartService.addItem(cmd2);

        shoppingCartService.removeItem(info2.getId());

        Item item1 = shoppingCartService.findItemById(info1.getId());
        Item item2 = shoppingCartService.findItemById(info2.getId());
        cart = shoppingCartService.findById(cart.getId());

        assertNotNull(item1);
        assertNull(item2);
        assertTrue( cart.getPrice().compareTo(new BigDecimal(50)) == 0);
    }

    @Test
    void checkoutValid() throws ServiceException, BudgetExceededException {

        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        List<CreateCategoryFromProductCmd> category = new ArrayList<CreateCategoryFromProductCmd>();

        category.add(new CreateCategoryFromProductCmd("Entertainment", "desc"));

        CreateProductCmd productCmd1 = new CreateProductCmd(new BigDecimal(10), "TV", "HDTV",
                10, category, null);

        CreateProductCmd productCmd2 = new CreateProductCmd(new BigDecimal(20), "PC", "Gaming",
                6, category, null);

        Product product1 = productService.save(productCmd1);
        Product product2 = productService.save(productCmd2);

        CreateItemCmd cmd1 = new CreateItemCmd(cart.getId(), product1.getId() ,5);
        CreateItemCmd cmd2 = new CreateItemCmd(cart.getId(), product2.getId() ,5);

        ItemInfo info1 = shoppingCartService.addItem(cmd1);
        ItemInfo info2 = shoppingCartService.addItem(cmd2);

        CreatePayPalAccountCmd createPayPalAccountCmd = new CreatePayPalAccountCmd("68984646", new BigDecimal(160),
                "SRB", LocalDate.now(), new Address(), user.getId() );

        PayPalAccount account = payPalAccountService.save(createPayPalAccountCmd);

        shoppingCartService.checkout(cart.getId());

        PayPalAccountInfo accountInfo = payPalAccountService.findById(account.getId());
        ProductInfo productInfo1 = productService.findById(product1.getId());
        ProductInfo productInfo2 = productService.findById(product2.getId());

        ShoppingCart cart1 = shoppingCartService.findById(cart.getId());

        assertTrue( accountInfo.getBudget().compareTo(new BigDecimal(10)) == 0 );
        assertEquals(5, productInfo1.getQuantity());
        assertEquals(1, productInfo2.getQuantity());
        assertEquals(Status.COMPLETED, cart1.getStatus());
    }

    @Test
    void checkoutBudgetExceeded() throws ServiceException, BudgetExceededException {

        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        List<CreateCategoryFromProductCmd> category = new ArrayList<CreateCategoryFromProductCmd>();

        category.add(new CreateCategoryFromProductCmd("Entertainment", "desc"));

        CreateProductCmd productCmd1 = new CreateProductCmd(new BigDecimal(10), "TV", "HDTV",
                10, category, null);

        CreateProductCmd productCmd2 = new CreateProductCmd(new BigDecimal(20), "PC", "Gaming",
                6, category, null);

        Product product1 = productService.save(productCmd1);
        Product product2 = productService.save(productCmd2);

        CreateItemCmd cmd1 = new CreateItemCmd(cart.getId(), product1.getId() ,5);
        CreateItemCmd cmd2 = new CreateItemCmd(cart.getId(), product2.getId() ,5);

        ItemInfo info1 = shoppingCartService.addItem(cmd1);
        ItemInfo info2 = shoppingCartService.addItem(cmd2);

        CreatePayPalAccountCmd createPayPalAccountCmd = new CreatePayPalAccountCmd("68984646", new BigDecimal(140),
                "SRB", LocalDate.now(), new Address(), user.getId() );

        PayPalAccount account = payPalAccountService.save(createPayPalAccountCmd);

        assertThrows(BudgetExceededException.class, () -> shoppingCartService.checkout(cart.getId()) );
    }

    @Test
    void checkoutServiceException() throws ServiceException, BudgetExceededException {

        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        List<CreateCategoryFromProductCmd> category = new ArrayList<CreateCategoryFromProductCmd>();

        category.add(new CreateCategoryFromProductCmd("Entertainment", "desc"));

        CreateProductCmd productCmd1 = new CreateProductCmd(new BigDecimal(10), "TV", "HDTV",
                4, category, null);

        CreateProductCmd productCmd2 = new CreateProductCmd(new BigDecimal(20), "PC", "Gaming",
                6, category, null);

        Product product1 = productService.save(productCmd1);
        Product product2 = productService.save(productCmd2);

        CreateItemCmd cmd1 = new CreateItemCmd(cart.getId(), product1.getId() ,5);
        CreateItemCmd cmd2 = new CreateItemCmd(cart.getId(), product2.getId() ,5);

        ItemInfo info1 = shoppingCartService.addItem(cmd1);
        ItemInfo info2 = shoppingCartService.addItem(cmd2);

        CreatePayPalAccountCmd createPayPalAccountCmd = new CreatePayPalAccountCmd("68984646", new BigDecimal(160),
                "SRB", LocalDate.now(), new Address(), user.getId() );

        PayPalAccount account = payPalAccountService.save(createPayPalAccountCmd);

        assertThrows(ServiceException.class, () -> shoppingCartService.checkout(cart.getId()) );
    }

    @Test
    void closeCart() throws ServiceException {
        CreateUserCmd userCmd = new CreateUserCmd("lukar", "passw", "Luka", "R");

        User user = userService.save(userCmd);

        CreateShoppingCartCmd shoppingCartCmd = new CreateShoppingCartCmd("cart", user.getId());

        ShoppingCart cart = shoppingCartService.save(shoppingCartCmd);

        shoppingCartService.closeShoppingCart(cart.getId());

        cart = shoppingCartService.findById( cart.getId() );

        assertNotNull(cart);
        assertEquals(user.getId(), cart.getUser().getId());
        assertEquals("cart", cart.getName());
        assertEquals(Status.INACTIVE, cart.getStatus());
        ShoppingCart finalCart = cart;
        assertThrows(ServiceException.class, () -> shoppingCartService.checkout(finalCart.getId()));
    }

}
