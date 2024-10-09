package org.graduate.shoefastbe.service.products;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.product.ProductDtoRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.AttributeEntity;
import org.graduate.shoefastbe.entity.BrandsEntity;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.graduate.shoefastbe.entity.SalesEntity;
import org.graduate.shoefastbe.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AttributeRepository attributeRepository;
    private final CustomRepository customRepository;
    private final SalesRepository salesRepository;
    private final BrandsRepository brandsRepository;
    @Override
    public Page<ProductDtoResponse> getAllProduct(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        List<AttributeEntity> attributeEntities = customRepository.getAttributeByProductId(productEntities
                .stream().map(ProductEntity::getId).collect(Collectors.toSet()));
        Map<Long, AttributeEntity> attributeMap = attributeEntities.stream().collect(Collectors.toMap(
                AttributeEntity::getProductId, Function.identity()
        ));
        List<BrandsEntity> brandsEntities = brandsRepository.findAllByIdIn(productEntities
                .stream()
                .map(ProductEntity::getBrandId)
                .collect(Collectors.toSet()));
        Map<Long, BrandsEntity> brandsEntityMap = brandsEntities.stream().collect(Collectors.toMap(
                BrandsEntity::getId,Function.identity()
        ));

        List<SalesEntity> salesEntities = salesRepository.findAllByIdIn(productEntities
                .stream()
                .map(ProductEntity::getSaleId)
                .collect(Collectors.toSet()));
        Map<Long, SalesEntity> salesEntityMap = salesEntities.stream().collect(Collectors.toMap(
                SalesEntity::getId,Function.identity()
        ));

        return productEntities.map(
                product -> {
                    AttributeEntity attribute = attributeMap.get(product.getId());
                    return ProductDtoResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(attribute.getPrice())
                            .brand(brandsEntityMap.get(product.getBrandId()).getName())
                            .code(product.getCode())
                            .view(product.getView())
                            .description(product.getDescription())
                            .image("image")
                            .discount(salesEntityMap.get(product.getSaleId()).getDiscount())
                            .isActive(product.getIsActive())
                            .build();
                }
        );
    }

    @Override
    public Page<ProductDtoResponse> getAllProductFilter(ProductDtoRequest productDtoRequest, Pageable pageable) {
        Page<AttributeEntity> attributeEntities = customRepository.getAttributeFilter(productDtoRequest.getBrandIds(),
                productDtoRequest.getCategoryIds(),productDtoRequest.getMin(),productDtoRequest.getMax(), pageable);
        Map<Long, ProductEntity> longProductEntityMap = productRepository.findAllByIdIn(attributeEntities.stream()
                .map(AttributeEntity::getProductId).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(
                        ProductEntity::getId,Function.identity()
                ));
        Map<Long, AttributeEntity> attributeMap = attributeEntities.stream().collect(Collectors.toMap(
                AttributeEntity::getProductId, Function.identity()
        ));
        List<BrandsEntity> brandsEntities = brandsRepository.findAllByIdIn(longProductEntityMap.values()
                .stream()
                .map(ProductEntity::getBrandId )
                .collect(Collectors.toSet()));
        Map<Long, BrandsEntity> brandsEntityMap = brandsEntities.stream().collect(Collectors.toMap(
                BrandsEntity::getId,Function.identity()
        ));

        List<SalesEntity> salesEntities = salesRepository.findAllByIdIn(longProductEntityMap.values()
                .stream()
                .map(ProductEntity::getSaleId)
                .collect(Collectors.toSet()));
        Map<Long, SalesEntity> salesEntityMap = salesEntities.stream().collect(Collectors.toMap(
                SalesEntity::getId,Function.identity()
        ));

        return attributeEntities.map(
                attribute -> {
                    ProductEntity product = longProductEntityMap.get(attribute.getProductId());
                    return ProductDtoResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(attribute.getPrice())
                            .brand(brandsEntityMap.get(product.getBrandId()).getName())
                            .code(product.getCode())
                            .view(product.getView())
                            .description(product.getDescription())
                            .image("image")
                            .discount(salesEntityMap.get(product.getSaleId()).getDiscount())
                            .isActive(product.getIsActive())
                            .build();
                }
        );
    }
}
