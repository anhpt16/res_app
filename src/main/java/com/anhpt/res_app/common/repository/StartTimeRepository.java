package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.StartTime;
import com.anhpt.res_app.common.enums.status.StartTimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StartTimeRepository extends JpaRepository<StartTime, Long>, JpaSpecificationExecutor<StartTime> {
    Optional<StartTime> findByTimeStart(LocalTime timeStart);

    List<StartTime> findByStatus(StartTimeStatus startTimeStatus);
}
