package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.graduate.shoefastbe.entity.Sales;
import org.mapstruct.Mapper;

@Mapper
public interface SaleMapper {
    SaleResponse getResponseBy(Sales sales);
    Sales getEntityBy(SaleResponse saleResponse);
}
