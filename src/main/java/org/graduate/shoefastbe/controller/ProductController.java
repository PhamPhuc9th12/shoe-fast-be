package org.graduate.shoefastbe.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.*;
import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.dto.product.CreateProductRequest;
import org.graduate.shoefastbe.dto.product.ProductDetailResponse;
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
    Page<ProductDtoResponse> getAllProduct(@ParameterObject Pageable pageable) {
        return productService.getAllProduct(pageable);
    }

    @PostMapping("/get-all/filter")
    @Operation(summary = "Lấy page product filter")
    Page<ProductDtoResponse> getAllProductFilter(@RequestBody ProductDtoRequest productDtoRequest,
                                                 @ParameterObject Pageable pageable) {
        return productService.getAllProductFilter(productDtoRequest, pageable);
    }

    @GetMapping()
    @Operation(summary = "Lấy chi tiết product")
    ProductDetailResponse getDetailProduct(@RequestParam Long id) {
        return productService.getProductDetail(id);
    }

    @GetMapping("/relate")
    @Operation(summary = "Lấy các sản phẩm liên quan")
    Page<ProductDtoResponse> getRelateProduct(@RequestParam Long id, @RequestParam Long brandId,
                                              @ParameterObject Pageable pageable) {
        return productService.getProductRelate(id, brandId, pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "Lấy các sản phẩm theo search")
    Page<ProductDtoResponse> getProductBySearch(@RequestParam String search,
                                                @ParameterObject Pageable pageable) {
        return productService.getProductBySearch(search, pageable);
    }
    //admin

    @GetMapping("/count")
    @Operation(summary = "Lấy số lượng product")
    Long countByProduct() {
        return productService.countProduct();
    }

    @GetMapping("/by-brand")
    Page<ProductDtoResponse> getAllProductByBrand(@RequestParam Long brandId,
                                                  @ParameterObject Pageable pageable) {
        return productService.getProductByBrand(brandId, pageable);
    }

    @PostMapping()
    ProductDtoResponse create(@RequestParam CreateProductRequest createProductRequest) {
        return productService.create(createProductRequest);
    }
}
