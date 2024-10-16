package org.graduate.shoefastbe.service.order;

import org.graduate.shoefastbe.dto.order.OrderDetailResponse;
import org.graduate.shoefastbe.dto.order.OrderDtoRequest;
import org.graduate.shoefastbe.dto.order.OrderDtoResponse;
import org.graduate.shoefastbe.entity.OrderDetailEntity;
import org.graduate.shoefastbe.entity.OrderEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService {
    OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest);
    OrderDtoResponse getOrderById(Long id);
    List<OrderDetailResponse> getOrderDetail(Long orderId);
}
