package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.category.CategoryRequest;
import org.graduate.shoefastbe.dto.category.CategoryResponse;
import org.graduate.shoefastbe.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface CategoryMapper {
    CategoryResponse getResponseBy(CategoryEntity category);
    CategoryEntity getEntityBy(CategoryRequest categoryRequest);
    void update(@MappingTarget CategoryEntity category, CategoryRequest categoryRequest);

}
