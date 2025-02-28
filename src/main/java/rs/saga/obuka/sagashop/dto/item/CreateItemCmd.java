package rs.saga.obuka.sagashop.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemCmd implements Serializable {

    private Long shoppingCartId;
    private Long productId;
    private Integer quantity;

}
