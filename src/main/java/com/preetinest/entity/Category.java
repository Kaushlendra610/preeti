package com.preetinest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = {"uuid", "slug"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 50, nullable = false)
    private String name; // e.g., "Event Management", "Travel Solutions"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; // Brief description of the category

    @Column(length = 255, nullable = false)
    private String metaTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String metaKeyword;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String metaDescription;

    @Column(length = 100, nullable = false)
    private String slug; // e.g., "event-management"

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true; // Category visibility

    @Column(nullable = false, columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus = 2; // Soft delete: 1 = deleted, 2 = not deleted

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<>(); // Subcategories under this category

}