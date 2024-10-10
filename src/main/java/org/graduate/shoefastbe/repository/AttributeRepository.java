package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Attr;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<AttributeEntity, Long> {
    List<AttributeEntity> findAllByProductIdAndSize(Long productId, Long size);
    List<AttributeEntity> findAllByProductId(Long productId);
    AttributeEntity findByProductIdAndSize(Long productId, Long size);
}
