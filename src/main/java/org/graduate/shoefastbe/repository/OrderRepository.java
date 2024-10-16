package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByAccountId(Long accountId, Pageable pageable);
    Page<OrderEntity> findAllByAccountIdAndOrderStatusId(Long accountId, Long orderStatusId, Pageable pageable);

}
