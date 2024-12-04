package org.graduate.shoefastbe.repository;

import org.graduate.shoefastbe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
        List<Category> findAllByIdIn(Collection<Long> ids);
}
