package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.domain.Product;
import rs.saga.obuka.sagashop.dto.product.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    Product createProductCmdToProduct(CreateProductCmd cmd);

    @Mapping(target = "id", ignore = true)
    Product createProductFromCategoryCmdToProduct(CreateProductFromCategoryCmd cmd);

    List<ProductResult> listProductToListProductResult(List<Product> products);

    ProductInfo productToProductInfo(Product product);

    void updateProductCmdToProduct(@MappingTarget Product product, UpdateProductCmd cmd);

//    List<ProductProjection> productToProductProjection(List<Product> products);

    @AfterMapping
    default void afterMappingDTO(Product product, @MappingTarget ProductInfo productInfo){
        AuditMapper.INSTANCE.fillAudit(product, productInfo.getAudit());
    }

}
