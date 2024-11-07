package org.graduate.shoefastbe.service.order;

import org.graduate.shoefastbe.dto.order.*;
import org.graduate.shoefastbe.dto.product.ProductReport;
import org.graduate.shoefastbe.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest);
    OrderDtoResponse getOrderById(Long id);
    List<OrderDetailResponse> getOrderDetail(Long orderId);
    List<OrderStatus> getAllOrderStatus();
    Page<OrderDtoResponse> getAllOrders (Long accountId, Long orderStatusId, Pageable pageable);
    OrderDtoResponse cancelOrder(CancelOrderRequest cancelOrderRequest);


    //admin
    List<CountResponse> getCountOrderByStatus();
    Long countOrder();
    List<YearSynthesis> getReportYear();
    Page<ProductReport> getReportByProduct(Pageable pageable);
    Page<OrderDtoResponse> getOrderByYearAndMonth(Long id,Long year, Long month, Pageable pageable);
    Page<OrderDtoResponse> getOrderByProduct(Long id, Pageable pageable);
    List<MonthSynthesis> getReportByMonth(Long year);
    OrderDtoResponse update(OrderUpdateRequest orderUpdateRequest);
    OrderDtoResponse cancelOrderAdmin(UpdateStatusOrderRequest orderUpdateRequest);
}
