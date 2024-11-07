package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByAccountId(Long accountId, Pageable pageable);
    Page<Order> findAllByAccountIdAndOrderStatusId(Long accountId, Long orderStatusId, Pageable pageable);

    List<Order> findAllByOrderStatusId(Long orderStatusId);

    @Query("SELECT o FROM Order o WHERE year(o.createDate) = :year and month(o.createDate) = :month")
    Page<Order> findOrderByYearAndMonth(@Param("year") Long year, @Param("month") Long month, Pageable pageable);
    @Query("SELECT o FROM Order o WHERE o.orderStatusId = :id and year(o.createDate) = :year and month(o.createDate) = :month")
    Page<Order> findOrderByOrderStatusAndYearAndMonth(@Param("id") Long id, @Param("year") Long year, @Param("month") Long month, Pageable pageable);

    @Query("SELECT o FROM Order o inner join OrderDetail d on o.id = d.orderId inner join Attribute a on a.id = d.attributeId inner join Product p on p.id = a.productId where p.id = :id and o.orderStatusId = 4")
    Page<Order> findOrderByProduct(@Param("id") Long id, Pageable pageable);
    @Query("SELECT o FROM Order o WHERE o.orderStatusId = 4 AND YEAR(o.createDate) = :year GROUP BY MONTH(o.createDate) ORDER BY SUM (o.total) DESC")
    List<Order> reportAmountMonth(@Param("year") Long year);
}
