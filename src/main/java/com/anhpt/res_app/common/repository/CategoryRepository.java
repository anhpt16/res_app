package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.enums.status.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    Page<Category> findByStatusOrderByCreatedAt(CategoryStatus status, Pageable pageable);
    Page<Category> findByOrderByCreatedAt(Pageable pageable);

    List<Category> findByStatus(CategoryStatus active);
}
