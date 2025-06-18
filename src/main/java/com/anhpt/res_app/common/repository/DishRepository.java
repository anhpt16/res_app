package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Category;
import com.anhpt.res_app.common.entity.Dish;
import com.anhpt.res_app.common.enums.status.DishStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
    Optional<Dish> findByName(String name);
    Page<Dish> findAll(Specification<Dish> search, Pageable pageable);
    List<Dish> findByCategoryAndStatus(Category category, DishStatus status, Sort publishedAt);
    List<Dish> findByStatusAndPublishedAtGreaterThanEqualOrderByPublishedAtDesc(DishStatus published, LocalDateTime numberDate);
}
