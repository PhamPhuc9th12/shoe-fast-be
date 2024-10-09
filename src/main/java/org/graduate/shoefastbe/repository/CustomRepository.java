package org.graduate.shoefastbe.repository;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.base.filter.Filter;
import org.graduate.shoefastbe.entity.AttributeEntity;
import org.graduate.shoefastbe.entity.ProductCategoryEntity;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class CustomRepository {
    private final EntityManager entityManager;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    public Page<ProductEntity> getAccountBy(String search, String role, Pageable pageable) {
        return Filter.builder(ProductEntity.class, entityManager)
                .filter()
                .isEqual("role", role)
                .isContain("username", search)
                .getPage(pageable);
    }
    public List<AttributeEntity> getAttributeByProductId(Collection<Long> productIds) {
         List<AttributeEntity> attributeEntities = Filter.builder(AttributeEntity.class, entityManager)
                .filter()
                .isEqual("size", 39L)
                .isIn("productId", productIds)
                 .getPage(PageRequest.of(0, 9999999)).getContent();
        if (attributeEntities.isEmpty()) {
            throw new RuntimeException(CodeAndMessage.ERR3);
        }
        return attributeEntities;
    }
    public Page<AttributeEntity> getAttributeFilter(Collection<Long> brandIds, Collection<Long> categoryIds,
                                                    Double min, Double max, Pageable pageable) {
        List<Long> productBrandIds = productRepository.findAllByBrandIdIn(brandIds).stream().map(ProductEntity::getId)
                .collect(Collectors.toList());
        List<Long> productCategoryIds = productCategoryRepository.findAllByCategoryIdIn(categoryIds).stream()
                .map(ProductCategoryEntity::getId)
                .collect(Collectors.toList());
        Set<Long> productIds = new HashSet<>(productBrandIds);
        productIds.addAll(productCategoryIds);
        Page<AttributeEntity> attributeEntities = Filter.builder(AttributeEntity.class, entityManager)
                .filter()
                .isEqual("size", 39L)
                .isIn("productId", productIds)
                .isGreaterThanOrEqual("price", min)
                .isLessThanOrEqual("price", max)
                .getPage(pageable);
        if (attributeEntities.isEmpty()) {
            throw new RuntimeException(CodeAndMessage.ERR3);
        }
        return attributeEntities;
    }

}
