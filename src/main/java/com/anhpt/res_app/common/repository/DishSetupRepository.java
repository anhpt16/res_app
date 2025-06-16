package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.DishSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DishSetupRepository extends JpaRepository<DishSetup, Long>, JpaSpecificationExecutor<DishSetup> {
    boolean existsByDishId(Long dishId);
}
