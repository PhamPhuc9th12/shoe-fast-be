package org.graduate.shoefastbe.service.brand;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.entity.BrandsEntity;
import org.graduate.shoefastbe.mapper.BrandsMapper;
import org.graduate.shoefastbe.repository.BrandsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {
    private final BrandsRepository brandsRepository;
    private final BrandsMapper brandsMapper;

    public Page<BrandResponse> getAllBrand(Pageable pageable) {
        Page<BrandsEntity> brandsEntities = brandsRepository.findAll(pageable);
        return brandsEntities.map(brandsMapper::getResponseBy);
    }
}
