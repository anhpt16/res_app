package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.StartTimeDuration;
import com.anhpt.res_app.common.entity.key.StartTimeDurationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartTimeDurationRepository extends JpaRepository<StartTimeDuration, StartTimeDurationId> {

}
