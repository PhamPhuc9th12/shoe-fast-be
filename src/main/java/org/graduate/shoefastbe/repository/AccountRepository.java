package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByUsername(String username);
    Account findByUsername(String username);
}
