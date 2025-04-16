package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.Item;
import rs.saga.obuka.sagashop.dto.item.CreateItemCmd;
import rs.saga.obuka.sagashop.dto.item.ItemInfo;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item createItemCmdToItem(CreateItemCmd cmd);

    @Mapping(source = "product.name", target = "name")
    ItemInfo itemToItemInfo(Item item);

}
