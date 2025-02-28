package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.ShoppingCart;

public class ShoppingCartBuilder {

    public static ShoppingCart buildShoppingCart() {
        return ShoppingCart.builder()
                .name("cart")
                .build();
    }

}
