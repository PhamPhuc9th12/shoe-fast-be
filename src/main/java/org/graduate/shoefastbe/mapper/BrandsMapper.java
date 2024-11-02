package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.brands.BrandRequest;
import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.entity.BrandsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface BrandsMapper {
    BrandResponse getResponseBy(BrandsEntity brandsEntity);
    BrandsEntity getEntityBy(BrandRequest brandRequest);
    void update(@MappingTarget BrandsEntity brands, BrandRequest brandRequest);
}
