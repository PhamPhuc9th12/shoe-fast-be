package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByAccountId(Long accountId, Pageable pageable);
    Page<Order> findAllByAccountIdAndOrderStatusId(Long accountId, Long orderStatusId, Pageable pageable);

    List<Order> findAllByOrderStatusId(Long orderStatusId);

}
