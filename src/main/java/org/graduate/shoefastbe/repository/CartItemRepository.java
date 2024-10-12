package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findCartItemByAccountIdAndAttributeId(Long accountId, Long attributeId);
    List<CartItemEntity> findByAccountIdAndIsActive(Long accountId,Boolean isActive);

}
