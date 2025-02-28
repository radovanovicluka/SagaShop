package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.Item;

public class ItemBuilder {

    public static Item itemPetTelefona() {
        return Item.builder()
                .product(ProductBuilder.telefonProduct())
                .quantity(5)
                .shoppingCart(ShoppingCartBuilder.buildShoppingCart())
                .build();
    }

}
