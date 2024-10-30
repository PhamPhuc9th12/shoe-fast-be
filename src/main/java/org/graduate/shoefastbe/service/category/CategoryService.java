package org.graduate.shoefastbe.service.category;

import org.graduate.shoefastbe.dto.category.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponse> getAllCategory(Pageable pageable);
}
