package rs.saga.obuka.sagashop.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author: Ana Dedović
 * Date: 28.06.2021.
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryCmd {

    private Long id;
    private String name;
    private String description;
    //    Id za dodavanje product-a
    private List<Long> addCategoryIds;
    //    Id za uklanjanje product-a
    private List<Long> removeCategoryIds;

}
