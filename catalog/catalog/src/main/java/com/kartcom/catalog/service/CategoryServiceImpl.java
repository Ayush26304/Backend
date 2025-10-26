
package com.kartcom.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kartcom.catalog.dto.CategoryDto;
import com.kartcom.catalog.entity.Category;
import com.kartcom.catalog.exception.ResourceNotFoundException;
import com.kartcom.catalog.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> dtos = new ArrayList<>();

        for (Category category : categories) {
            CategoryDto dto = new CategoryDto();
            BeanUtils.copyProperties(category, dto);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        CategoryDto dto = new CategoryDto();
        BeanUtils.copyProperties(optional.get(), dto);
        return dto;
    }

    @Override
    public Category addCategory(CategoryDto dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDto dto) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        Category category = optional.get();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
