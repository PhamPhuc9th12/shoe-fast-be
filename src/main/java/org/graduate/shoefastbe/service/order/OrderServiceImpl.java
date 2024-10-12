package org.graduate.shoefastbe.service.order;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.order.OrderDtoRequest;
import org.graduate.shoefastbe.dto.order.OrderDtoResponse;
import org.graduate.shoefastbe.repository.OrderDetailRepository;
import org.graduate.shoefastbe.repository.OrderRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest) {
        return null;
    }
}
