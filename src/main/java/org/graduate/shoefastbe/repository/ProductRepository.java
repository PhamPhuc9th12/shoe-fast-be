package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByBrandIdIn(Collection<Long> brandIds);
    List<Product> findAllByIdIn(Collection<Long> productIds);
    Product findByCode(String code);
    List<Product> findAllBySaleId(Long saleId);

    Page<Product> findAllByBrandIdAndIsActive(Long productId, Boolean isActive, Pageable pageable);
    @Query("SELECT p FROM Product p" +
            " inner join Attribute a on p.id = a.productId " +
            "where LOWER(p.name) like LOWER(CONCAT('%', :search, '%'))" +
            " or LOWER(p.code) like LOWER(CONCAT('%', :search, '%'))")
    Page<Product> getProductBySearch(@Param("search") String search, Pageable pageable);

}
