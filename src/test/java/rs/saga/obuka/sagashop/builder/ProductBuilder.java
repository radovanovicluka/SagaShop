package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.Product;

import java.math.BigDecimal;

public class ProductBuilder {

    public static Product telefonProduct() {
        return Product.builder()
                .name("Telefon")
                .price(new BigDecimal(500))
                .description("Veoma telefon")
                .quantity(20)
                .build();
    }

    public static Product misProduct() {
        return Product.builder()
                .name("Mis")
                .price(new BigDecimal(50))
                .description("Veoma mis")
                .quantity(10)
                .build();
    }

    public static Product sveskaProduct() {
        return Product.builder()
                .name("Sveska")
                .price(new BigDecimal(10))
                .description("Veoma sveska")
                .quantity(5)
                .build();
    }

}
