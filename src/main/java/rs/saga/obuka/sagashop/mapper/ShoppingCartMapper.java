package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.ShoppingCart;
import rs.saga.obuka.sagashop.dto.shoppingcart.CreateShoppingCartCmd;

@Mapper
public interface ShoppingCartMapper {

    ShoppingCartMapper INSTANCE = Mappers.getMapper(ShoppingCartMapper.class);

    ShoppingCart createShoppingCartToShoppingCart(CreateShoppingCartCmd shoppingCart);

}
