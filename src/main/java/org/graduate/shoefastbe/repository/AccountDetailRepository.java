package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.AccountDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailRepository extends JpaRepository<AccountDetailEntity, Long> {
    Boolean existsByEmail(String email);
}
