package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.entity.BrandsEntity;
import org.mapstruct.Mapper;

@Mapper
public interface BrandsMapper {
    BrandResponse getResponseBy(BrandsEntity brandsEntity);
}
