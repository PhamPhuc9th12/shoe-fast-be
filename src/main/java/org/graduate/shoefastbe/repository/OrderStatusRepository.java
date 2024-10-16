package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatusEntity, Long> {
    OrderStatusEntity findByName(String name);
    List<OrderStatusEntity> findAllByIdIn(Collection<Long> ids);
}
