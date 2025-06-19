package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.DishSetup;
import com.anhpt.res_app.common.enums.status.DishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishSetupRepository extends JpaRepository<DishSetup, Long>, JpaSpecificationExecutor<DishSetup> {
    boolean existsByDishId(Long dishId);

    List<DishSetup> findByCurrentStatusAndNextStatusOrderByMilestoneAsc(DishStatus active, DishStatus published);

    Optional<DishSetup> findByDishId(Long dishId);

    List<DishSetup> findByMilestone(LocalDateTime milestone);
}
