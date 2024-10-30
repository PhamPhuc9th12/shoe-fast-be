package org.graduate.shoefastbe.service.products;

import org.graduate.shoefastbe.dto.product.CreateProductRequest;
import org.graduate.shoefastbe.dto.product.ProductDetailResponse;
import org.graduate.shoefastbe.dto.product.ProductDtoRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface ProductService {
    Page<ProductDtoResponse> getAllProduct(Pageable pageable);

    Page<ProductDtoResponse> getAllProductFilter(ProductDtoRequest productDtoRequest, Pageable pageable);

    ProductDetailResponse getProductDetail(Long productId);
    Page<ProductDtoResponse> getProductRelate(Long productId, Long brandId, Pageable pageable);
    Page<ProductDtoResponse> getProductBySearch(String search, Pageable pageable);
    Long countProduct();
    Page<ProductDtoResponse> getProductByBrand(Long brandId, Pageable pageable);

    //admin
    ProductDtoResponse create( CreateProductRequest createProductRequest);

}
