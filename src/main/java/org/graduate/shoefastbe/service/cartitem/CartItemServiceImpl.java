package org.graduate.shoefastbe.service.cartitem;

import lombok.AllArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.dto.cart.CartItemDetailResponse;
import org.graduate.shoefastbe.dto.cart.CartItemDtoRequest;
import org.graduate.shoefastbe.dto.cart.CartItemDtoResponse;
import org.graduate.shoefastbe.entity.*;
import org.graduate.shoefastbe.mapper.CartItemMapper;
import org.graduate.shoefastbe.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService{
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final AttributeRepository attributeRepository;
    private final AccountRepository accountRepository;
    private final SalesRepository salesRepository;
    private final ProductRepository productRepository;
    @Override
    public CartItemDtoResponse modifyCartItem(CartItemDtoRequest cartItemDtoRequest) {
        AttributeEntity attributeEntity = attributeRepository.findById(cartItemDtoRequest.getAttributeId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        AccountEntity account = accountRepository.findById(cartItemDtoRequest.getAccountId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        CartItemEntity cartItem = cartItemRepository.findCartItemByAccountIdAndAttributeId(cartItemDtoRequest.getAccountId(),
                cartItemDtoRequest.getAttributeId());
        if(Objects.isNull(cartItem)){
            if(cartItemDtoRequest.getQuantity() > attributeEntity.getStock()){
                throw new RuntimeException(CodeAndMessage.ERR5);
            }else{
                CartItemEntity cartItemEntity = CartItemEntity.builder()
                        .accountId(account.getId())
                        .attributeId(attributeEntity.getId())
                        .quantity(cartItemDtoRequest.getQuantity())
                        .lastPrice(cartItemDtoRequest.getLastPrice())
                        .isActive(Boolean.TRUE)
                        .build();
                 cartItemRepository.save(cartItemEntity);
                 return cartItemMapper.getResponseFrom(cartItemEntity);
            }
        }else{
            long flag = cartItemDtoRequest.getQuantity();
            if(flag == 0){
                cartItem.setQuantity(flag);
                cartItem.setIsActive(Boolean.FALSE);
                cartItemRepository.save(cartItem);
                return cartItemMapper.getResponseFrom(cartItem);
            }else if(flag > attributeEntity.getStock()){
                throw new RuntimeException(CodeAndMessage.ERR5);
            }else{
                cartItem.setQuantity(flag);
                cartItem.setIsActive(Boolean.TRUE);
                cartItemRepository.save(cartItem);
                return cartItemMapper.getResponseFrom(cartItem);
            }
        }
    }

    @Override
    public Boolean isEnoughStock(Long id, Long quantity) {
        AttributeEntity attribute = attributeRepository.findById(id).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        if(attribute.getStock() < quantity){
            throw new RuntimeException(CodeAndMessage.ERR5);
        }
        return true;
    }

    @Override
    public List<CartItemDetailResponse> getCartItemDetailByAccount(Long id) {
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByAccountIdAndIsActive(id,Boolean.TRUE);
        Map<Long, AttributeEntity> attributeMap = attributeRepository.findAllByIdIn(cartItemEntities
                .stream()
                .map(CartItemEntity::getAttributeId)
                .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(
                        AttributeEntity::getId, Function.identity()
                ));

        Set<Long> productIds = attributeMap.values().stream().map(AttributeEntity::getProductId).collect(Collectors.toSet());
        List<ProductEntity> productEntities = productRepository.findAllByIdIn(productIds);
        Map<Long, Long> productSaleMap = productEntities.stream().collect(Collectors.toMap(
                ProductEntity::getId, ProductEntity::getSaleId
        ));
        Map<Long, SalesEntity> salesEntityMap = salesRepository.findAllByIdIn(productEntities
                .stream()
                .map(ProductEntity::getSaleId)
                .collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(
                        SalesEntity::getId,Function.identity()
                ));

        return cartItemEntities.stream().map(
                cartItemEntity -> {
                    AttributeEntity attribute = attributeMap.get(cartItemEntity.getAttributeId());
                    return CartItemDetailResponse
                            .builder()
                            .size(attribute.getSize())
                            .stock(attribute.getStock())
                            .price(attribute.getPrice())
                            .name(attribute.getName())
                            .image("image")
                            .discount(salesEntityMap.get(productSaleMap.get(attribute.getProductId())).getDiscount())
                            .quantity(cartItemEntity.getQuantity())
                            .lastPrice(cartItemEntity.getLastPrice())
                            .build();
                }
        ).collect(Collectors.toList());
    }
}
