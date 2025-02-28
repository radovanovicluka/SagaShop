package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.Category;
import rs.saga.obuka.sagashop.dto.category.CategoryInfo;
import rs.saga.obuka.sagashop.dto.category.CategoryResult;
import rs.saga.obuka.sagashop.dto.category.CreateCategoryCmd;
import rs.saga.obuka.sagashop.dto.category.UpdateCategoryCmd;

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

    List<CategoryResult> listCategoryToListCategoryResult(List<Category> categories);

    CategoryInfo categoryToCategoryInfo(Category category);

    void updateCategoryCmdToCategory(@MappingTarget Category category, UpdateCategoryCmd cmd);
}
