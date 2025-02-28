package rs.saga.obuka.sagashop.builder;

import rs.saga.obuka.sagashop.domain.Category;

/**
 * @author: Ana Dedović
 * Date: 08.07.2021.
 **/
public class CategoryBuilder {

    public static Category categoryBelaTehnika() {
        return Category.builder()
                .name("Bela tehnika").description("Bela tehnika - sve na jednom mestu")
                .build();
    }

    public static Category categoryRacunari() {
        return Category.builder()
                .name("Računari i komponente").description("Računari i komponente - sve na jednom mestu")
                .build();
    }

}
