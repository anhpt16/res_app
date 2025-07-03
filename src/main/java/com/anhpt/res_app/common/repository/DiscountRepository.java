package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long>, JpaSpecificationExecutor<Discount> {
    boolean existsByDishId(Long dishId);

    // Tìm discount có timeStart trong khoảng thời gian
    @Query("SELECT d FROM Discount d WHERE d.timeStart BETWEEN :startTime AND :endTime")
    List<Discount> findByTimeStartBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    // Tìm discount có timeEnd trong khoảng thời gian
    @Query("SELECT d FROM Discount d WHERE d.timeEnd BETWEEN :startTime AND :endTime")
    List<Discount> findByTimeEndBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Discount> findByTimeStart(LocalDateTime now);
    List<Discount> findByTimeEnd(LocalDateTime now);

    // Tìm các discount đã hết hạn (timeEnd < thời gian hiện tại)
    @Query("SELECT d FROM Discount d WHERE d.timeEnd < :currentTime")
    List<Discount> findExpiredDiscounts(@Param("currentTime") LocalDateTime currentTime);

    // Tìm các discount đang hoạt động (thời gian hiện tại nằm trong khoảng timeStart và timeEnd)
    @Query("SELECT d FROM Discount d WHERE d.timeStart <= :currentTime AND d.timeEnd >= :currentTime")
    List<Discount> findActiveDiscounts(@Param("currentTime") LocalDateTime currentTime);
}
