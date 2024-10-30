package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.category.CategoryResponse;
import org.graduate.shoefastbe.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryResponse getResponseBy(CategoryEntity category);
}
