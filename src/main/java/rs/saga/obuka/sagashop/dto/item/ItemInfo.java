package rs.saga.obuka.sagashop.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Koje informacije je protrebno vratiti?
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {

    private Long id;
    private String name;
    private Integer quantity;

}
