package com.preetinest.service;



import com.preetinest.dto.request.CategoryRequestDTO;
import com.preetinest.dto.response.CategoryResponseDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);
    Optional<CategoryResponseDTO> getCategoryById(Long id);
    Optional<CategoryResponseDTO> getCategoryByUuid(String uuid);
    Optional<CategoryResponseDTO> getCategoryBySlug(String slug);
    List<CategoryResponseDTO> getAllActiveCategories();
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);
    void softDeleteCategory(Long id, Long userId);
}