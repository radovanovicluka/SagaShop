package rs.saga.obuka.sagashop.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.saga.obuka.sagashop.domain.ShoppingCart;
import rs.saga.obuka.sagashop.dto.item.CreateItemCmd;
import rs.saga.obuka.sagashop.dto.item.ItemInfo;
import rs.saga.obuka.sagashop.dto.shoppingcart.CreateShoppingCartCmd;
import rs.saga.obuka.sagashop.exception.BudgetExceededException;
import rs.saga.obuka.sagashop.exception.ServiceException;
import rs.saga.obuka.sagashop.service.ShoppingCartService;

import javax.validation.Valid;

@RestController
@RequestMapping("/shoppingcart")
@RequiredArgsConstructor
public class ShoppingCartRest {

    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCart createShoppingCart(@RequestBody @Valid CreateShoppingCartCmd cmd) throws ServiceException {
        return shoppingCartService.save(cmd);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ItemInfo addItem(@RequestBody @Valid CreateItemCmd cmd) throws ServiceException {
        return shoppingCartService.addItem(cmd);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable Long id) throws ServiceException {
        shoppingCartService.removeItem(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void checkout(@PathVariable Long id) throws ServiceException, BudgetExceededException {
        shoppingCartService.checkout(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void close(@PathVariable Long id) throws ServiceException {
        shoppingCartService.closeShoppingCart(id);
    }

}
