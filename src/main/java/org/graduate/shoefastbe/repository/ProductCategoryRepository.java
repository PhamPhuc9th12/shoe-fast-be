package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    List<ProductCategoryEntity> findAllByCategoryIdIn(Collection<Long> ids);
    List<ProductCategoryEntity> findAllByProductId(Long productId);
}
