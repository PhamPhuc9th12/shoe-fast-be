package org.graduate.shoefastbe.mapper;

import org.graduate.shoefastbe.dto.cart.CartItemDtoRequest;
import org.graduate.shoefastbe.dto.cart.CartItemDtoResponse;
import org.graduate.shoefastbe.entity.CartItemEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CartItemMapper {
    CartItemEntity getEntityByRequest(CartItemDtoRequest cartItemDtoRequest);
    CartItemDtoResponse getResponseFrom(CartItemEntity cartItemEntity);
}
