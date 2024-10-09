package org.graduate.shoefastbe.repository;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.base.error_success_handle.CodeAndMessage;
import org.graduate.shoefastbe.base.filter.Filter;
import org.graduate.shoefastbe.entity.AttributeEntity;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
@AllArgsConstructor
public class CustomRepository {
    private final EntityManager entityManager;
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
}
