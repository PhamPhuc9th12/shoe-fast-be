package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.order.OrderDtoRequest;
import org.graduate.shoefastbe.dto.order.OrderDtoResponse;
import org.graduate.shoefastbe.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    OrderEntity getOrderByRequest(OrderDtoRequest orderDtoRequest);
    OrderDtoResponse getResponseByEntity(OrderEntity orderEntity);
}
