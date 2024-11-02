package org.graduate.shoefastbe.service.category;

import lombok.AllArgsConstructor;
import org.graduate.shoefastbe.dto.category.CategoryRequest;
import org.graduate.shoefastbe.dto.category.CategoryResponse;
import org.graduate.shoefastbe.entity.Category;
import org.graduate.shoefastbe.mapper.CategoryMapper;
import org.graduate.shoefastbe.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public Page<CategoryResponse> getAllCategory(Pageable pageable) {
        Page<Category> entities = categoryRepository.findAll(pageable);
        return entities.map(categoryMapper::getResponseBy);
    }

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {
        Category category = categoryMapper.getEntityBy(categoryRequest);
        categoryRepository.save(category);
        return categoryMapper.getResponseBy(category);
    }
}
