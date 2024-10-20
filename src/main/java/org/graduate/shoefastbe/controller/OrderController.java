package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.Get;
import org.graduate.shoefastbe.dto.order.*;
import org.graduate.shoefastbe.dto.product.ProductReport;
import org.graduate.shoefastbe.entity.OrderDetailEntity;
import org.graduate.shoefastbe.entity.OrderStatusEntity;
import org.graduate.shoefastbe.service.order.OrderService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Tạo mới order")
    OrderDtoResponse createOrder(@RequestBody OrderDtoRequest orderDtoRequest) {
        return orderService.createOrder(orderDtoRequest);
    }
    @GetMapping()
    @Operation(summary = "Lấy thông tin đơn hàng")
    OrderDtoResponse getOrderById(@RequestParam Long id){
        return orderService.getOrderById(id);
    }
    @GetMapping("/order-detail")
    @Operation(summary = "Lấy thông tin chi tiết đơn hàng")
    List<OrderDetailResponse> getOrderDetail(@RequestParam Long orderId){
        return orderService.getOrderDetail(orderId);
    }

    @GetMapping("/order-status")
    @Operation(summary = "Lấy thông tin chi tiết trạng thái đơn hàng")
    List<OrderStatusEntity> getAllOrderStatus(){
        return orderService.getAllOrderStatus();
    }
    @GetMapping("/list")
    @Operation(summary = "Lấy danh sách đơn hàng")
    Page<OrderDtoResponse> getAllOrderStatus(@RequestParam Long accountId,
                                             @RequestParam Long orderStatusId,
                                             @ParameterObject Pageable pageable){
        return orderService.getAllOrders(accountId, orderStatusId, pageable);
    }
    @PostMapping("/cancel")
    @Operation(summary = "Xóa đơn hàng")
    OrderDtoResponse cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest){
        return orderService.getCancelOrder(cancelOrderRequest);
    }

    // admin
    @GetMapping("/list/count")
    @Operation(summary = "Lấy số lượng đơn hàng")
    List<CountResponse> getAllOrderCount(){
        return orderService.getCountOrderByStatus();
    }
    @GetMapping("/count")
    @Operation(summary = "Đếm số lượng đơn hàng")
    Long getCountOrder(){
        return orderService.countOrder();
    }
    @GetMapping("/synthesis/year")
    @Operation(summary = "Lấy thống kê theo năm")
    List<YearSynthesis> getYearSynthesis(){
        return orderService.getReportYear();
    }
    @GetMapping("/synthesis/year")
    @Operation(summary = "Lấy thống kê theo năm")
    Page<ProductReport> getReportProduct(@ParameterObject Pageable pageable){
        return orderService.getReportByProduct(pageable);
    }


}
