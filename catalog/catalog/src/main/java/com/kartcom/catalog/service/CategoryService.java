package com.kartcom.catalog.service;

import java.util.List;

import com.kartcom.catalog.dto.CategoryDto;
import com.kartcom.catalog.entity.Category;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    Category addCategory(CategoryDto dto);
    Category updateCategory(Long id, CategoryDto dto);
    void deleteCategory(Long id);
}
