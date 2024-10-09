package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.*;
import org.graduate.shoefastbe.dto.product.ProductDtoRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.service.products.ProductService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/get-all")
    @Operation(summary = "Lấy page product")
    Page<ProductDtoResponse> getAllProduct(@ParameterObject Pageable pageable){
        return productService.getAllProduct(pageable);
    }

    @PostMapping("/get-all/filter")
    @Operation(summary = "Lấy page product filter")
    Page<ProductDtoResponse> getAllProductFilter(@RequestBody ProductDtoRequest productDtoRequest,
                                                 @ParameterObject Pageable pageable){
        return productService.getAllProductFilter(productDtoRequest,pageable);
    }
}
