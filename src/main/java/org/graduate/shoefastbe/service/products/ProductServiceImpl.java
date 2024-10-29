package org.graduate.shoefastbe.service.products;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.common.Common;
import org.graduate.shoefastbe.dto.product.ProductDetailResponse;
import org.graduate.shoefastbe.dto.product.ProductDtoRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.*;
import org.graduate.shoefastbe.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ProductCategoryRepository productCategoryRepository;
    @Override
    public Page<ProductDtoResponse> getAllProduct(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        return getProductDtoResponses(productEntities);
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
                            .image(Common.DEFAULT_IMAGE)
                            .discount(salesEntityMap.get(product.getSaleId()).getDiscount())
                            .isActive(product.getIsActive())
                            .build();
                }
        );
    }

    @Override
    public ProductDetailResponse getProductDetail(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        List<AttributeEntity> attributeEntities = attributeRepository.findAllByProductId(productId);
        Double price = (double) 0;
        for(AttributeEntity attribute: attributeEntities){
            if(attribute.getSize().equals(Common.SIZE_AVG)){
                price = attribute.getPrice();
            }
        }
        List<ProductCategoryEntity> categoryEntities = productCategoryRepository.findAllByProductId(productId);
        BrandsEntity brandsEntity = brandsRepository.findById(product.getBrandId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        SalesEntity salesEntity = salesRepository.findById(product.getSaleId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        List<String> imgURLs = new ArrayList<>();
        for(int i = 0; i<6 ; i++){
            imgURLs.add(Common.DEFAULT_IMAGE);
        }
        return ProductDetailResponse.builder()
                .attributes(attributeEntities)
                .main(Common.DEFAULT_IMAGE)
                .price(price)
                .brandId(product.getBrandId())
                .categoryIds(categoryEntities.stream().map(ProductCategoryEntity::getCategoryId).collect(Collectors.toList()))
                .images(imgURLs)
                .saleId(product.getSaleId())
                .brand(brandsEntity.getName())
                .code(product.getCode())
                .description(product.getDescription())
                .discount(salesEntity.getDiscount())
                .id(productId)
                .name(product.getName())
                .view(product.getView())
                .build();
    }

    @Override
    public Page<ProductDtoResponse> getProductRelate(Long productId, Long brandId, Pageable pageable) {
        Page<ProductEntity> productEntities = customRepository.getProductRelate(productId,brandId,pageable);
        return getProductDtoResponses(productEntities);
    }

    @Override
    public Page<ProductDtoResponse> getProductBySearch(String search, Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.getProductBySearch(search,pageable);
        return getProductDtoResponses(productEntities);
    }

    @Override
    public Long countProduct() {
        return productRepository.count();
    }

    @Override
    public Page<ProductDtoResponse> getProductByBrand(Long brandId, Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAllByBrandIdAndIsActive(brandId,Boolean.TRUE,pageable);
        return getProductDtoResponses(productEntities);
    }

    private Page<ProductDtoResponse> getProductDtoResponses(Page<ProductEntity> productEntities) {
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
                            .image(Common.DEFAULT_IMAGE)
                            .discount(salesEntityMap.get(product.getSaleId()).getDiscount())
                            .isActive(product.getIsActive())
                            .build();
                }
        );
    }
}
