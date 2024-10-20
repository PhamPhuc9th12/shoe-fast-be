package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByAccountId(Long accountId, Pageable pageable);
    Page<OrderEntity> findAllByAccountIdAndOrderStatusId(Long accountId, Long orderStatusId, Pageable pageable);
//    @Query("SELECT o FROM OrderEntity o WHERE YEAR(o.createDate) = YEAR(CURRENT_TIMESTAMP)")
//    List<OrderEntity> findAllByYear();

}
