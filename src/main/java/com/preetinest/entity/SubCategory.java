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
@Table(name = "subcategories", uniqueConstraints = @UniqueConstraint(columnNames = {"uuid", "slug"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String uuid;

    @Column(length = 50, nullable = false)
    private String name; // e.g., "Corporate Meetings", "Conferences"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; // Brief description of the subcategory

    @Column(length = 255, nullable = false)
    private String metaTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String metaKeyword;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String metaDescription;

    @Column(length = 100, nullable = false)
    private String slug; // e.g., "corporate-meetings"

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true; // Subcategory visibility

    @Column(nullable = false, columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus = 2; // Soft delete: 1 = deleted, 2 = not deleted

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Parent category

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Service> services = new ArrayList<>(); // Services under this subcategory

}