package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.graduate.shoefastbe.entity.SalesEntity;
import org.mapstruct.Mapper;

@Mapper
public interface SaleMapper {
    SaleResponse getResponseBy(SalesEntity salesEntity);
}
