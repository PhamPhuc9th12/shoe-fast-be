package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.BrandsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BrandsRepository extends JpaRepository<BrandsEntity, Long> {
    List<BrandsEntity> findAllByIdIn(Collection<Long> ids);
}
