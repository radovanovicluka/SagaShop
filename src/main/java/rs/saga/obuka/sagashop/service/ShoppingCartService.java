package rs.saga.obuka.sagashop.service;

import rs.saga.obuka.sagashop.domain.Item;
import rs.saga.obuka.sagashop.domain.ShoppingCart;
import rs.saga.obuka.sagashop.dto.item.CreateItemCmd;
import rs.saga.obuka.sagashop.dto.item.ItemInfo;
import rs.saga.obuka.sagashop.dto.shoppingcart.CreateShoppingCartCmd;
import rs.saga.obuka.sagashop.exception.BudgetExceededException;
import rs.saga.obuka.sagashop.exception.ServiceException;

public interface ShoppingCartService {

    ShoppingCart save(CreateShoppingCartCmd cmd) throws ServiceException;

    ItemInfo addItem(CreateItemCmd cmd) throws ServiceException;

    void removeItem(Long itemId) throws ServiceException;

    void closeShoppingCart(Long cartId) throws ServiceException;

    void checkout(Long cartId) throws ServiceException, BudgetExceededException;

    //    Za potrebe testiranja
    ShoppingCart findById(Long id);

    Item findItemById(Long itemId);

    void deleteAll() throws ServiceException;
}
