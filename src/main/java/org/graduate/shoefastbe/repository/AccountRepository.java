package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Boolean existsByUsername(String username);
    AccountEntity findByUsername(String username);
}
