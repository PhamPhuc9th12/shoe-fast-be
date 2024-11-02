package org.graduate.shoefastbe.service.brand;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.dto.brands.BrandRequest;
import org.graduate.shoefastbe.dto.brands.BrandResponse;
import org.graduate.shoefastbe.entity.BrandsEntity;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.graduate.shoefastbe.mapper.BrandsMapper;
import org.graduate.shoefastbe.repository.BrandsRepository;
import org.graduate.shoefastbe.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {
    private final BrandsRepository brandsRepository;
    private final BrandsMapper brandsMapper;
    private final ProductRepository productRepository;

    @Override
    public Page<BrandResponse> getAllBrand(Pageable pageable) {
        Page<BrandsEntity> brandsEntities = brandsRepository.findAll(pageable);
        return brandsEntities.map(brandsMapper::getResponseBy);
    }

    @Override
    @Transactional
    public BrandResponse create(BrandRequest brandRequest) {
        BrandsEntity brandsEntity = brandsMapper.getEntityBy(brandRequest);
        brandsRepository.save(brandsEntity);
        return brandsMapper.getResponseBy(brandsEntity);
    }

    @Override
    public BrandResponse getDetail(Long id) {
        BrandsEntity brandsEntity = brandsRepository.findById(id).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        return brandsMapper.getResponseBy(brandsEntity);
    }

    @Override
    public BrandResponse update(BrandRequest brandRequest) {
        BrandsEntity brandsEntity = brandsRepository.findById(brandRequest.getId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        brandsMapper.update(brandsEntity, brandRequest);
        brandsRepository.save(brandsEntity);
        List<ProductEntity> productEntities = productRepository.findAllByBrandIdIn(Collections.singleton(brandsEntity.getId()));
        for(ProductEntity product : productEntities){
            if(Boolean.FALSE.equals(brandsEntity.getIsActive())){
                product.setIsActive(Boolean.FALSE);
            }else{
                product.setIsActive(Boolean.TRUE);
            }
            productRepository.save(product);
        }
        return brandsMapper.getResponseBy(brandsEntity);
    }
}
