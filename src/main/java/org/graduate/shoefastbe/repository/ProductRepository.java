package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.dto.product.ProductDtoResponse;
import org.graduate.shoefastbe.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByBrandIdIn(Collection<Long> brandIds);
    List<ProductEntity> findAllByIdIn(Collection<Long> productIds);
    @Query("SELECT p FROM ProductEntity p" +
            " inner join AttributeEntity a on p.id = a.productId " +
            "where LOWER(p.name) like LOWER(CONCAT('%', :search, '%'))" +
            " or LOWER(p.code) like LOWER(CONCAT('%', :search, '%'))")
    Page<ProductEntity> getProductBySearch(@Param("search") String search, Pageable pageable);

}
