package org.graduate.shoefastbe.service.products;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.common.Common;
import org.graduate.shoefastbe.dto.category.AttributeDtoRequest;
import org.graduate.shoefastbe.dto.product.CreateProductRequest;
import org.graduate.shoefastbe.dto.product.ProductDetailResponse;
import org.graduate.shoefastbe.dto.product.ProductDtoRequest;
import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.*;
import org.graduate.shoefastbe.mapper.ProductMapper;
import org.graduate.shoefastbe.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
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
    private final ImageRepository imageRepository;
    private final BrandsRepository brandsRepository;
    private final ProductMapper productMapper;
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

    @Override
    @Transactional
    public ProductDtoResponse create(CreateProductRequest createProductRequest) {
        ProductEntity product = productRepository.findByCode(createProductRequest.getCode());
        if(Objects.nonNull(product)){
            throw new RuntimeException(CodeAndMessage.ERR9);
        }
        /*Create product from data*/
        ProductEntity productEntity = ProductEntity.builder()
                .createDate(LocalDate.now())
                .modifyDate(LocalDate.now())
                .name(createProductRequest.getName())
                .code(createProductRequest.getCode())
                .description(createProductRequest.getDescription())
                .view(1L)
                .brandId(createProductRequest.getBrandId())
                .saleId(createProductRequest.getSaleId())
                .isActive(Boolean.TRUE)
                .build();
        productRepository.save(productEntity);

        List<Long> categoryIds = createProductRequest.getCategoryId();
        for(Long id: categoryIds){
            ProductCategoryEntity productCategory = ProductCategoryEntity.builder()
                    .categoryId(id)
                    .productId(productEntity.getId())
                    .build();
            productCategoryRepository.save(productCategory);

        }
        /*Create image of product*/
        List<String> imageUrl = createProductRequest.getImageUrl();
        for(int i = 0; i < imageUrl.size(); i++){
            ImageEntity image = new ImageEntity();
            if(i == 0){
                image.setName("main");
            }else{
                image.setName("other");
            }
            image.setImageLink(imageUrl.get(i));
            image.setCrateDate(LocalDate.now());
            image.setModifyDate(LocalDate.now());
            image.setIsActive(Boolean.TRUE);
            image.setProductId(productEntity.getId());
            imageRepository.save(image);
        }
        /*Create attribute of product*/
        List<AttributeDtoRequest> reqAttributeDtos = createProductRequest.getAttribute();
        for(AttributeDtoRequest r: reqAttributeDtos){
            AttributeEntity attribute = new AttributeEntity();
            attribute.setName(productEntity.getName());
            attribute.setSize(r.getSize());
            attribute.setPrice(r.getPrice());
            attribute.setStock(r.getStock());
            attribute.setCache(0L);
            attribute.setCreateDate(LocalDate.now());
            attribute.setModifyDate(LocalDate.now());
            attribute.setProductId(productEntity.getId());
            attributeRepository.save(attribute);
        }
        return productMapper.getResponseFromEntity(productEntity);
    }

    @Override
    public ProductDtoResponse update(CreateProductRequest createProductRequest) {
        ProductEntity productEntity = productRepository.findById(createProductRequest.getId()).orElseThrow(
                () -> new RuntimeException(CodeAndMessage.ERR3)
        );
        productMapper.update(productEntity, createProductRequest);
        productEntity.setView(1L);
        productEntity.setIsActive(Boolean.TRUE);
        productRepository.save(productEntity);

        List<Long> categoryIds = createProductRequest.getCategoryId();
        for(Long id: categoryIds){
            ProductCategoryEntity productCategory = productCategoryRepository.findById(id).orElseThrow(
                    () -> new RuntimeException(CodeAndMessage.ERR3)
            );
            productCategory.setProductId(productEntity.getId());
            productCategory.setCategoryId(id);
            productCategoryRepository.save(productCategory);

        }
        /*Create attribute of product*/
        List<AttributeDtoRequest> reqAttributeDtos = createProductRequest.getAttribute();
        for(AttributeDtoRequest r: reqAttributeDtos){
            AttributeEntity attribute = attributeRepository.findByProductIdAndSize(productEntity.getId(),39L);
            if(Objects.nonNull(attribute)){
                attribute.setStock(r.getStock());
                attribute.setSize(r.getSize());
                attribute.setPrice(r.getPrice());
                attributeRepository.save(attribute);
            }else{
                attribute = new AttributeEntity();
                attribute.setName(productEntity.getName());
                attribute.setSize(r.getSize());
                attribute.setPrice(r.getPrice());
                attribute.setStock(r.getStock());
                attribute.setCache(0L);
                attribute.setCreateDate(LocalDate.now());
                attribute.setModifyDate(LocalDate.now());
                attribute.setProductId(productEntity.getId());
                attributeRepository.save(attribute);
            }
        }
        return productMapper.getResponseFromEntity(productEntity);
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
