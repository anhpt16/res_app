package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.ComboVersionDish;
import com.anhpt.res_app.common.entity.key.ComboVersionDishId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboVersionDishRepository extends JpaRepository<ComboVersionDish, Long> {
    boolean existsById(ComboVersionDishId id);
    Optional<ComboVersionDish> findById(ComboVersionDishId id);
}
