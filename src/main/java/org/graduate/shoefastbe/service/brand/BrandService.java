package org.graduate.shoefastbe.service.brand;

import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    Page<BrandResponse> getAllBrand(Pageable pageable);
}