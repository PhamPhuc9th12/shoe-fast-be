package org.graduate.shoefastbe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.*;
import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.dto.category.AttributeDtoRequest;
import org.graduate.shoefastbe.dto.product.CreateProductRequest;
import org.graduate.shoefastbe.dto.product.ProductDetailResponse;
import org.graduate.shoefastbe.dto.product.ProductDtoRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.service.products.ProductService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin
@AllArgsConstructor
//@CrossOrigin(origins = "https://25fd-116-101-91-180.ngrok-free.app")
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

@PostMapping("/create")
public ProductDtoResponse create(@ModelAttribute CreateProductRequest createProductRequest,
                                 @RequestPart("files") List<MultipartFile> multipartFileList) throws IOException {
    return productService.create(createProductRequest, multipartFileList);
}


    @PostMapping("/modify")
    ProductDtoResponse update(@RequestParam CreateProductRequest createProductRequest) {
        return productService.update(createProductRequest);
    }
}
