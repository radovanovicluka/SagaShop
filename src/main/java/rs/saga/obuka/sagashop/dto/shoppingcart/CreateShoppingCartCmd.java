package rs.saga.obuka.sagashop.dto.shoppingcart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateShoppingCartCmd implements Serializable {

    private String name;
    private Long userId;

}
