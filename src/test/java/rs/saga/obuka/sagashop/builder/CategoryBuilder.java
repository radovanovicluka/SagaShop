package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.Category;

import java.util.ArrayList;

/**
 * @author: Ana Dedović
 * Date: 08.07.2021.
 **/
public class CategoryBuilder {

    public static Category categoryBelaTehnika() {
        return Category.builder()
                .categoryName("Bela tehnika").description("Bela tehnika - sve na jednom mestu")
                .products(new ArrayList<>())
                .build();
    }

    public static Category categoryRacunari() {
        return Category.builder()
                .categoryName("Računari i komponente").description("Računari i komponente - sve na jednom mestu")
                .products(new ArrayList<>())
                .build();
    }

}
