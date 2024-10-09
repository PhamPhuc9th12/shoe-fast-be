package org.graduate.shoefastbe.service.products;

import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


public interface ProductService {
    Page<ProductDtoResponse> getAllProduct(Pageable pageable);
}
