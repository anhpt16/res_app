package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Duration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Long>, JpaSpecificationExecutor<Duration> {
    Optional<Duration> findByDuration(Integer duration);

    Page<Duration> findAll(Specification<Duration> specification, Pageable pageable);
}
