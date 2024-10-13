package org.graduate.shoefastbe.service.order;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.common.enums.OrderStatusEnum;
import org.graduate.shoefastbe.dto.order.OrderDtoRequest;
import org.graduate.shoefastbe.dto.order.OrderDtoResponse;
import org.graduate.shoefastbe.entity.*;
import org.graduate.shoefastbe.mapper.OrderMapper;
import org.graduate.shoefastbe.repository.*;
import org.graduate.shoefastbe.util.MailUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AccountRepository accountRepository;
    private final OrderMapper orderMapper;
    private final OrderStatusRepository orderStatusRepository;
    private final VoucherRepository voucherRepository;
    private final AttributeRepository attributeRepository;
    private final NotificationRepository notificationRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    @Transactional
    public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest) {
        AccountEntity account = accountRepository.findById(orderDtoRequest.getAccountId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        OrderStatusEntity orderStatusEntity = orderStatusRepository.findByName(OrderStatusEnum.WAIT_ACCEPT.name());
        OrderEntity orderEntity = orderMapper.getOrderByRequest(orderDtoRequest);
        orderEntity.setOrderStatusId(orderStatusEntity.getId());
        orderEntity.setSeen(Boolean.FALSE);
        orderEntity.setAccountId(account.getId());

        if(Objects.nonNull(orderDtoRequest.getCode())){
            VoucherEntity voucherEntity = voucherRepository.findVoucherByCode(orderDtoRequest.getCode());
            voucherEntity.setCount(voucherEntity.getCount() -1);
            voucherRepository.save(voucherEntity);
            orderEntity.setVoucherId(voucherEntity.getId());
        }

        orderEntity.setEncodeUrl(null);
        orderRepository.save(orderEntity);
        //create orderDetail
        createDetailOrder(orderDtoRequest);
        // send notification
        sendNotification(orderEntity);

        return orderMapper.getResponseByEntity(orderEntity);
    }
    private void createDetailOrder(OrderDtoRequest orderDtoRequest){
        List<AttributeEntity> attributeEntities = attributeRepository.findAllByIdIn(
                orderDtoRequest.getOrderDetails().stream().map(OrderDetailEntity::getAttributeId).collect(Collectors.toSet())
        );
        Map<Long, AttributeEntity> attributeEntityMap = attributeEntities.stream().collect(Collectors.toMap(
                AttributeEntity::getId, Function.identity()
        ));

        for(OrderDetailEntity orderDetail : orderDtoRequest.getOrderDetails()){
            AttributeEntity attribute = attributeEntityMap.get(orderDetail.getAttributeId());
            if(attribute.getStock() < orderDetail.getQuantity()){
                throw new RuntimeException(CodeAndMessage.ERR5);
            }else{
                attribute.setStock(attribute.getStock() - orderDetail.getQuantity());
                attribute.setCache(attribute.getCache() + orderDetail.getQuantity());
                attributeRepository.save(attribute);
                orderDetail.setOrderId(orderDetail.getOrderId());
                orderDetailRepository.save(orderDetail);
                if(Objects.nonNull(orderDtoRequest.getAccountId())){
                    CartItemEntity cartItemEntity = cartItemRepository
                            .findCartItemByAccountIdAndAttributeId(orderDtoRequest.getAccountId(), orderDetail.getAttributeId());
                    cartItemEntity.setQuantity(0L);
                    cartItemEntity.setIsActive(Boolean.FALSE);
                    cartItemRepository.save(cartItemEntity);
                }
            }
        }
    }

    private void sendNotification(OrderEntity orderEntity){
        NotificationEntity notificationEntity = NotificationEntity
                .builder()
                .type(1L)
                .order_id(orderEntity.getId())
                .content(String.format("Đơn hàng số %s vừa được tạo, xác nhận ngay nào", orderEntity.getId()))
                .deliver(Boolean.FALSE)
                .read(Boolean.FALSE)
                .build();
        notificationRepository.save(notificationEntity);
        try {
            MailUtil.sendEmailOrder(orderEntity);
        } catch (MessagingException e) {
            System.out.println("Can't send an email.");
        }
    }
}
