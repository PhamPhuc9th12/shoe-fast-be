package org.graduate.shoefastbe.controller;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.sale.SaleResponse;
import org.graduate.shoefastbe.service.sale.SaleService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sale")
@AllArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping("/list")
    public Page<SaleResponse> getAllSale(@ParameterObject Pageable pageable){
        return saleService.getAllSale(pageable);
    }
}
