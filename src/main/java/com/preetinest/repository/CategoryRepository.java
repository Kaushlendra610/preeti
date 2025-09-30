package com.preetinest.repository;

import com.preetinest.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByUuid(String uuid);
    Optional<Category> findBySlug(String slug);
    List<Category> findByDeleteStatusAndActive(int deleteStatus, boolean active);
}