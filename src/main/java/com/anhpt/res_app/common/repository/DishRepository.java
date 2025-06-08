package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    boolean existsByName(String name);
    Optional<Dish> findByName(String name);

    Page<Dish> findAll(Specification<Dish> search, Pageable pageable);
}
