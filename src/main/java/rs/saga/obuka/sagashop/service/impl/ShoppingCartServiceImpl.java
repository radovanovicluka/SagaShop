package rs.saga.obuka.sagashop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.saga.obuka.sagashop.dao.ItemDAO;
import rs.saga.obuka.sagashop.dao.ProductDAO;
import rs.saga.obuka.sagashop.dao.ShoppingCartDAO;
import rs.saga.obuka.sagashop.dao.UserDAO;
import rs.saga.obuka.sagashop.domain.*;
import rs.saga.obuka.sagashop.dto.item.CreateItemCmd;
import rs.saga.obuka.sagashop.dto.item.ItemInfo;
import rs.saga.obuka.sagashop.dto.shoppingcart.CreateShoppingCartCmd;
import rs.saga.obuka.sagashop.exception.BudgetExceededException;
import rs.saga.obuka.sagashop.exception.DAOException;
import rs.saga.obuka.sagashop.exception.ErrorCode;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.mapper.ItemMapper;
import rs.saga.obuka.sagashop.mapper.ShoppingCartMapper;
import rs.saga.obuka.sagashop.service.ShoppingCartService;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartDAO shoppingCartDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final ItemDAO itemDAO;

    @Override
    public ShoppingCart save(CreateShoppingCartCmd cmd) throws ServiceException {
        ShoppingCart cart = ShoppingCartMapper.INSTANCE.createShoppingCartToShoppingCart(cmd);

        cart.setStatus(Status.NEW);

        User user = userDAO.findOne(cmd.getUserId());

        if (user == null) {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "User not found");
        }

        cart.setUser(user);

        try {
            cart = shoppingCartDAO.save(cart);
        }
        catch (DAOException e) {
            log.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, e.getMessage());
        }

        return cart;
    }

    @Override
    public ItemInfo addItem(CreateItemCmd cmd) throws ServiceException {
        Item item = ItemMapper.INSTANCE.createItemCmdToItem(cmd);

        ShoppingCart cart = shoppingCartDAO.findOne(cmd.getShoppingCartId());

        if ( cart == null ) {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "Shopping cart not found");
        }
        else if ( cart.getStatus() == Status.INACTIVE || cart.getStatus() == Status.COMPLETED ) {
            throw new ServiceException(ErrorCode.ERR_GEN_005, "Shopping cart is inactive");
        }

        item.setShoppingCart(cart);

        if ( cart.getStatus() == Status.NEW ) {
            cart.setStatus(Status.ACTIVE);
        }

        Product product = productDAO.findOne(cmd.getProductId());

        if ( product == null ) {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "Product not found");
        }

        item.setProduct(product);

        cart.setPrice(cart.getPrice().add(product.getPrice().multiply( new BigDecimal(item.getQuantity()))) );

        try {
            item = itemDAO.save(item);
        }
        catch (DAOException e) {
            log.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_001, e.getMessage());
        }

        return ItemMapper.INSTANCE.itemToItemInfo(item);
    }

    @Override
    public void removeItem(Long itemId) throws ServiceException {

        Item item = itemDAO.findOne(itemId);

        if ( item == null ) {
            throw new ServiceException(ErrorCode.ERR_GEN_002, "Item not found");
        }

        BigDecimal price = item.getProduct().getPrice().multiply( new BigDecimal(item.getQuantity()));

        item.getShoppingCart().setPrice(item.getShoppingCart().getPrice().subtract(price));

        try {
            itemDAO.delete(item);
        }
        catch (DAOException e) {
            log.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_003, e.getMessage());
        }

    }

    @Override
    public void closeShoppingCart(Long cartId) throws ServiceException {

        ShoppingCart cart = shoppingCartDAO.findOne(cartId);

        if ( cart == null ) {
            throw new ServiceException( ErrorCode.ERR_GEN_002, "Shopping cart not found! ID = " + cartId );
        }

        cart.setStatus(Status.INACTIVE);

    }

//    Da li se dobro primenjuju izmene?
    @Override
    public void checkout(Long cartId) throws ServiceException, BudgetExceededException {

        ShoppingCart cart = shoppingCartDAO.findOne(cartId);

        if ( cart == null ) {
            throw new ServiceException( ErrorCode.ERR_GEN_002, "Shopping cart not found! ID = " + cartId );
        }

        if ( cart.getStatus() != Status.ACTIVE ) {
            throw new ServiceException( ErrorCode.ERR_GEN_005, "Shopping cart is inactive!" );
        }

        if ( cart.getUser().getPayPalAccount() == null ) {
            throw new ServiceException( ErrorCode.ERR_GEN_005, "No PayPal account for user! User ID = " + cart.getUser().getId() );
        }

        if ( cart.getUser().getPayPalAccount().getBudget().compareTo(cart.getPrice()) < 0 ) {
            throw new BudgetExceededException();
        }

        quantityUpdate(cart);

        BigDecimal budget = cart.getUser( ).getPayPalAccount( ).getBudget( ).subtract( cart.getPrice( ) );
        cart.getUser( ).getPayPalAccount( ).setBudget( budget );

        cart.setStatus(Status.COMPLETED);
    }

    @Override
    public ShoppingCart findById(Long id) {
        return shoppingCartDAO.findOne(id);
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemDAO.findOne(itemId);
    }

    @Override
    public void deleteAll() throws ServiceException {
        try {
            itemDAO.deleteAll();
            shoppingCartDAO.deleteAll();
        } catch (DAOException e) {
            log.error(e.getMessage());
            throw new ServiceException(ErrorCode.ERR_GEN_005, e.getMessage());
        }
    }

    private void quantityUpdate( ShoppingCart cart ) throws ServiceException {

        for ( Item item : cart.getItems() ) {

            if ( item.getQuantity() > item.getProduct().getQuantity() ) {
                throw new ServiceException(ErrorCode.ERR_GEN_005, "Not enough items in storage");
            }

            item.getProduct().setQuantity(item.getProduct().getQuantity() - item.getQuantity());
        }

    }

}
