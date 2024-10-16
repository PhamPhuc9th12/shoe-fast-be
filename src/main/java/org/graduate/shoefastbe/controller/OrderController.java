package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.Get;
import org.graduate.shoefastbe.dto.order.OrderDetailResponse;
import org.graduate.shoefastbe.dto.order.OrderDtoRequest;
import org.graduate.shoefastbe.dto.order.OrderDtoResponse;
import org.graduate.shoefastbe.entity.OrderDetailEntity;
import org.graduate.shoefastbe.service.order.OrderService;
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

}
