package com.preetinest.impl;

import com.preetinest.dto.request.CategoryRequestDTO;
import com.preetinest.dto.response.CategoryResponseDTO;
import com.preetinest.entity.Category;
import com.preetinest.entity.SubCategory;
import com.preetinest.repository.CategoryRepository;
import com.preetinest.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setUuid(UUID.randomUUID().toString());
        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        category.setMetaTitle(requestDTO.getMetaTitle());
        category.setMetaKeyword(requestDTO.getMetaKeyword());
        category.setMetaDescription(requestDTO.getMetaDescription());
        category.setSlug(requestDTO.getSlug());
        category.setActive(requestDTO.isActive());
        category.setDeleteStatus(2);

        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDTO(savedCategory);
    }

    @Override
    public Optional<CategoryResponseDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .filter(c -> c.getDeleteStatus() == 2)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Optional<CategoryResponseDTO> getCategoryByUuid(String uuid) {
        return categoryRepository.findByUuid(uuid)
                .filter(c -> c.getDeleteStatus() == 2)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Optional<CategoryResponseDTO> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .filter(c -> c.getDeleteStatus() == 2)
                .map(this::mapToResponseDTO);
    }

    @Override
    public List<CategoryResponseDTO> getAllActiveCategories() {
        return categoryRepository.findByDeleteStatusAndActive(2, true)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        category.setMetaTitle(requestDTO.getMetaTitle());
        category.setMetaKeyword(requestDTO.getMetaKeyword());
        category.setMetaDescription(requestDTO.getMetaDescription());
        category.setSlug(requestDTO.getSlug());
        category.setActive(requestDTO.isActive());

        Category updatedCategory = categoryRepository.save(category);
        return mapToResponseDTO(updatedCategory);
    }

    @Override
    public void softDeleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .filter(c -> c.getDeleteStatus() == 2)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        category.setDeleteStatus(1);
        categoryRepository.save(category);
        // Note: userId can be used for logging/auditing purposes
    }

    private CategoryResponseDTO mapToResponseDTO(Category category) {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setId(category.getId());
        responseDTO.setUuid(category.getUuid());
        responseDTO.setName(category.getName());
        responseDTO.setDescription(category.getDescription());
        responseDTO.setMetaTitle(category.getMetaTitle());
        responseDTO.setMetaKeyword(category.getMetaKeyword());
        responseDTO.setMetaDescription(category.getMetaDescription());
        responseDTO.setSlug(category.getSlug());
        responseDTO.setActive(category.isActive());
        responseDTO.setCreatedAt(category.getCreatedAt());
        responseDTO.setUpdatedAt(category.getUpdatedAt());
        responseDTO.setSubCategoryIds(category.getSubCategories().stream()
                .map(SubCategory::getId)
                .collect(Collectors.toList()));
        return responseDTO;
    }
}
