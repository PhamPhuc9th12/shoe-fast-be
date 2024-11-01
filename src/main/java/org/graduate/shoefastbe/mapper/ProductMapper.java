package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.product.CreateProductRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProductMapper {
    ProductDtoResponse getResponseFromEntity(ProductEntity product);
    void update (@MappingTarget  ProductEntity product, CreateProductRequest createProductRequest);
}
