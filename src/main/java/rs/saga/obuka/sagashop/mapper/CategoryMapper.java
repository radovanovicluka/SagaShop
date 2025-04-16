package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.*;

import java.util.List;

/**
 * @author: Ana Dedović
 * Date: 28.06.2021.
 **/
@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "id", ignore = true)
    Category createCategoryCmdToCategory(CreateCategoryCmd cmd);

    @Mapping(target = "id", ignore = true)
    Category createCategoryFromProductCmdToCategory(CreateCategoryFromProductCmd cmd);

    List<CategoryResult> listCategoryToListCategoryResult(List<Category> categories);

    CategoryInfo categoryToCategoryInfo(Category category);

    void updateCategoryCmdToCategory(@MappingTarget Category category, UpdateCategoryCmd cmd);

}
