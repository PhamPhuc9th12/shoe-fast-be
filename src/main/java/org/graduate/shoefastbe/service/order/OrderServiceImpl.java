package org.graduate.shoefastbe.service.order;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.common.enums.OrderStatusEnum;
import org.graduate.shoefastbe.dto.order.CancelOrderRequest;
import org.graduate.shoefastbe.dto.order.OrderDetailResponse;
import org.graduate.shoefastbe.dto.order.OrderDtoRequest;
import org.graduate.shoefastbe.dto.order.OrderDtoResponse;
import org.graduate.shoefastbe.entity.*;
import org.graduate.shoefastbe.mapper.OrderMapper;
import org.graduate.shoefastbe.repository.*;
import org.graduate.shoefastbe.util.MailUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
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
        OrderStatusEntity orderStatusEntity = orderStatusRepository.findByName(OrderStatusEnum.WAIT_ACCEPT.getValue());
        OrderEntity orderEntity = orderMapper.getOrderByRequest(orderDtoRequest);
        orderEntity.setOrderStatusId(orderStatusEntity.getId());
        orderEntity.setSeen(Boolean.FALSE);
        orderEntity.setAccountId(account.getId());
        orderEntity.setCreateDate(LocalDate.now());
        orderEntity.setModifyDate(LocalDate.now());

        if (Objects.nonNull(orderDtoRequest.getCode()) && !orderDtoRequest.getCode().isEmpty()) {
            VoucherEntity voucherEntity = voucherRepository.findVoucherByCode(orderDtoRequest.getCode());
            voucherEntity.setCount(voucherEntity.getCount() - 1);
            voucherRepository.save(voucherEntity);
            orderEntity.setVoucherId(voucherEntity.getId());
        }

        orderEntity.setEncodeUrl(null);
        orderRepository.save(orderEntity);
        //create orderDetail
        createDetailOrder(orderDtoRequest, orderEntity);
        // send notification
        CompletableFuture.runAsync(() -> {
            try {
                sendNotification(orderEntity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return orderMapper.getResponseByEntity(orderEntity);
    }

    @Override
    public OrderDtoResponse getOrderById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        return orderMapper.getResponseByEntity(orderEntity);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetail(Long orderId) {
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAllByOrderId(orderId);
        List<AttributeEntity> attributeEntities = attributeRepository.findAllByIdIn(orderDetailEntities
                .stream()
                .map(OrderDetailEntity::getAttributeId)
                .collect(Collectors.toList()));
        Map<Long, AttributeEntity> attributeMap = attributeEntities.stream().collect(Collectors.toMap(
                AttributeEntity::getId, Function.identity()
        ));
        return orderDetailEntities.stream().map(
                orderDetailEntity -> OrderDetailResponse
                        .builder()
                        .id(orderDetailEntity.getId())
                        .quantity(orderDetailEntity.getQuantity())
                        .sellPrice(orderDetailEntity.getSellPrice())
                        .originPrice(orderDetailEntity.getOriginPrice())
                        .orderId(orderDetailEntity.getOrderId())
                        .attributeSize(attributeMap.get(orderDetailEntity.getAttributeId()).getSize())
                        .attributeId(orderDetailEntity.getAttributeId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderStatusEntity> getAllOrderStatus() {
        return orderStatusRepository.findAll();
    }

    @Override
    public Page<OrderDtoResponse> getAllOrders(Long accountId, Long orderStatusId, Pageable pageable) {
//        OrderStatusEntity orderStatus = orderStatusRepository.findById(orderStatusId).orElseThrow(
//                () -> new RuntimeException(CodeAndMessage.ERR3)
//        );
        if (orderStatusId == 0) {
            Page<OrderDtoResponse> orderEntities = orderRepository.findAllByAccountId(accountId, pageable).map(orderMapper::getResponseByEntity);
            List<OrderStatusEntity> orderStatusEntities = orderStatusRepository.findAllByIdIn(orderEntities.stream().map(OrderDtoResponse::getOrderStatusId).collect(Collectors.toList()));
            Map<Long, OrderStatusEntity> orderStatusEntityMap = orderStatusEntities.stream()
                    .collect(Collectors.toMap(OrderStatusEntity::getId, Function.identity()));
            return orderEntities.map(
                    orderDtoResponse -> {
                        orderDtoResponse.setOrderStatusName(orderStatusEntityMap.get(orderDtoResponse.getOrderStatusId()).getName());
                        return orderDtoResponse;
                    }
            );

        }
        Page<OrderEntity> orderEntityList = orderRepository.findAllByAccountIdAndOrderStatusId(accountId, orderStatusId, pageable);
        Page<OrderDtoResponse> orderEntities = orderEntityList.map(orderMapper::getResponseByEntity);
        List<OrderStatusEntity> orderStatusEntities = orderStatusRepository.findAllByIdIn(orderEntities.stream().map(OrderDtoResponse::getOrderStatusId).collect(Collectors.toList()));
        Map<Long, OrderStatusEntity> orderStatusEntityMap = orderStatusEntities.stream()
                .collect(Collectors.toMap(OrderStatusEntity::getId, Function.identity()));
        return orderEntities.map(
                orderDtoResponse -> {
                    orderDtoResponse.setOrderStatusName(orderStatusEntityMap.get(orderDtoResponse.getOrderStatusId()).getName());
                    return orderDtoResponse;
                }
        );
    }

    @Override
    @Transactional
    public OrderDtoResponse getCancelOrder(CancelOrderRequest cancelOrderRequest) {
        OrderEntity order = orderRepository.findById(cancelOrderRequest.getId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        OrderStatusEntity orderStatus = orderStatusRepository.findById(order.getOrderStatusId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        if (orderStatus.getName().equals(OrderStatusEnum.IS_DELIVERY.getValue())) {
            throw new RuntimeException("Đơn hàng đang được vận chuyển.");
        } else if (orderStatus.getName().equals(OrderStatusEnum.CANCELED.getValue())) {
            throw new RuntimeException("Đơn hàng đã được hủy.");
        } else if (orderStatus.getName().equals(OrderStatusEnum.DELIVERED.getValue())) {
            throw new RuntimeException("Đơn hàng đã được giao thành công.");
        }
        OrderStatusEntity orderStatusCancel = orderStatusRepository.findByName(OrderStatusEnum.CANCELED.getValue());
        order.setOrderStatusId(orderStatusCancel.getId());
        order.setDescription(cancelOrderRequest.getDescription());
        order = orderRepository.save(order);

        Collection<OrderDetailEntity> orderDetails = orderDetailRepository.findAllByOrderId(order.getId());
        for (OrderDetailEntity orderDetail : orderDetails) {
            AttributeEntity attribute = attributeRepository.findById(orderDetail.getAttributeId()).orElseThrow(
                    () -> new RuntimeException(CodeAndMessage.ERR3)
            );
            attribute.setStock(attribute.getStock() + orderDetail.getQuantity());
            attribute.setCache(attribute.getCache() - orderDetail.getQuantity());
            attributeRepository.save(attribute);
        }
        if(Objects.nonNull(order.getVoucherId())){
            VoucherEntity voucher = voucherRepository.findById(order.getVoucherId()).orElseThrow(
                    () -> new RuntimeException(CodeAndMessage.ERR3)
            );
            if (voucher != null) {
                voucher.setCount(1L);
                voucher.setIsActive(Boolean.TRUE);
                voucherRepository.save(voucher);
            }
        }
        NotificationEntity notification = NotificationEntity.builder()
                .read(Boolean.FALSE)
                .deliver(Boolean.FALSE)
                .type(2L)
                .content(String.format("Đơn hàng %s vừa hủy, kiểm tra ngay nào", order.getId()))
                .orderId(order.getId())
                .build();
        notificationRepository.save(notification);
        return orderMapper.getResponseByEntity(order);
    }

    private void createDetailOrder(OrderDtoRequest orderDtoRequest, OrderEntity orderEntity) {
        List<AttributeEntity> attributeEntities = attributeRepository.findAllByIdIn(
                orderDtoRequest.getOrderDetails().stream().map(OrderDetailEntity::getAttributeId).collect(Collectors.toSet())
        );
        Map<Long, AttributeEntity> attributeEntityMap = attributeEntities.stream().collect(Collectors.toMap(
                AttributeEntity::getId, Function.identity()
        ));

        for (OrderDetailEntity orderDetail : orderDtoRequest.getOrderDetails()) {
            AttributeEntity attribute = attributeEntityMap.get(orderDetail.getAttributeId());
            if (attribute.getStock() < orderDetail.getQuantity()) {
                throw new RuntimeException(CodeAndMessage.ERR5);
            } else {
                attribute.setStock(attribute.getStock() - orderDetail.getQuantity());
                attribute.setCache(attribute.getCache() + orderDetail.getQuantity());
                attributeRepository.save(attribute);
                orderDetail.setOrderId(orderEntity.getId());
                orderDetailRepository.save(orderDetail);
                if (Objects.nonNull(orderDtoRequest.getAccountId())) {
                    CartItemEntity cartItemEntity = cartItemRepository
                            .findCartItemByAccountIdAndAttributeId(orderDtoRequest.getAccountId(), orderDetail.getAttributeId());
                    cartItemEntity.setQuantity(0L);
                    cartItemEntity.setIsActive(Boolean.FALSE);
                    cartItemRepository.save(cartItemEntity);
                }
            }
        }
    }

    private void sendNotification(OrderEntity orderEntity) {
        NotificationEntity notificationEntity = NotificationEntity
                .builder()
                .type(1L)
                .orderId(orderEntity.getId())
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
