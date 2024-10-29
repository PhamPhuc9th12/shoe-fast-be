package org.graduate.shoefastbe.controller;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.service.brand.BrandService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brand")
@AllArgsConstructor
public class BrandsController {
    private final BrandService brandService;

    @GetMapping("/list")
    Page<BrandResponse> getAllBrands(@ParameterObject Pageable pageable) {
        return brandService.getAllBrand(pageable);
    }

}
