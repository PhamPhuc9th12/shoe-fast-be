package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductDtoResponse getResponseFromEntity(ProductEntity product);
}
