package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    VoucherEntity findVoucherByCode(String code);
}
