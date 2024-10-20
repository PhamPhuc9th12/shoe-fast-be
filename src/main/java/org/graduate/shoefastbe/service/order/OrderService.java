package org.graduate.shoefastbe.service.order;

import org.graduate.shoefastbe.dto.order.*;
import org.graduate.shoefastbe.entity.OrderDetailEntity;
import org.graduate.shoefastbe.entity.OrderEntity;
import org.graduate.shoefastbe.entity.OrderStatusEntity;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService {
    OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest);
    OrderDtoResponse getOrderById(Long id);
    List<OrderDetailResponse> getOrderDetail(Long orderId);
    List<OrderStatusEntity> getAllOrderStatus();
    Page<OrderDtoResponse> getAllOrders (Long accountId, Long orderStatusId, Pageable pageable);
    OrderDtoResponse getCancelOrder(CancelOrderRequest cancelOrderRequest);

    //admin
    List<CountResponse> getCountOrderByStatus();
    Long countOrder();
    List<YearSynthesis> getReportYear();
}
