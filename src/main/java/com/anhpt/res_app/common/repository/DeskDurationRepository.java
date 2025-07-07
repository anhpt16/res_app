package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.DeskDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeskDurationRepository extends JpaRepository<DeskDuration, Long>, JpaSpecificationExecutor<DeskDuration> {
    Optional<DeskDuration> findByDeskNumberAndDurationId(Integer deskNumber, Long durationId);
}
