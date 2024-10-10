package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.attribute.AttributeDtoResponse;
import org.graduate.shoefastbe.entity.AttributeEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AttributeMapper {
    AttributeDtoResponse getResponseFromEntity(AttributeEntity attribute);
}
